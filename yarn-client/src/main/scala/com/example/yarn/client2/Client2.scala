package com.example.yarn.client2

import java.util.Collections

import org.apache.hadoop.fs.Path
import org.apache.hadoop.yarn.api.ApplicationConstants
import org.apache.hadoop.yarn.api.records.{ContainerLaunchContext, _}
import org.apache.hadoop.yarn.client.api.YarnClient
import org.apache.hadoop.yarn.conf.YarnConfiguration
import org.apache.hadoop.yarn.util.Records

import scala.collection.JavaConverters._

/**
 * Entry point into the Yarn application.
 *
 * This is a YARN client which launches an Application master by adding the jar to local resources
 *
 */
object Client2 {

  val jarPath = "hdfs://54.92.87.167/myJars/yarn-App-assembly-1.0.jar"
  val internalJarPath = "hdfs://172.31.3.50/myJars/yarn-App-assembly-1.0.jar"
  val numberOfInstances = 1

  def main(args: Array[String]) {

    implicit val conf = new YarnConfiguration()

    conf.set(YarnConfiguration.RM_ADDRESS, "54.92.87.167")
    conf.set("fs.defaultFS", "hdfs://54.92.87.167/")

    // start a yarn client
    val client = YarnClient.createYarnClient()
    client.init(conf)
    client.start()

    // application creation
    val app = client.createApplication()
    val amContainer = Records.newRecord(classOf[ContainerLaunchContext])
    //application master is a just java program with given commands
    amContainer.setCommands(List(
      "$JAVA_HOME/bin/java" +
        " -Xmx256M" +
        " com.example.yarn.app2.ApplicationMaster"+
        "  " +internalJarPath +"   "+ numberOfInstances + " "+
        " 1>" + ApplicationConstants.LOG_DIR_EXPANSION_VAR + "/stdout" +
        " 2>" + ApplicationConstants.LOG_DIR_EXPANSION_VAR + "/stderr"
    ).asJava)


    //add the jar which contains the Application master code to classpath
    val appMasterJar = Records.newRecord(classOf[LocalResource])
    Utils.setUpLocalResource(new Path(jarPath), appMasterJar)
    amContainer.setLocalResources(Collections.singletonMap("helloworld.jar", appMasterJar))

    //setup env to get all yarn and hadoop classes in classpath
    val env = collection.mutable.Map[String, String]()
    Utils.setUpEnv(env)
    amContainer.setEnvironment(env.asJava)

    //specify resource requirements
    val resource = Records.newRecord(classOf[Resource])
    resource.setMemory(300)
    resource.setVirtualCores(1)

    //context to launch
    val appContext = app.getApplicationSubmissionContext
    appContext.setApplicationName("helloworld")
    appContext.setAMContainerSpec(amContainer)
    appContext.setResource(resource)
    appContext.setQueue("default")

    //submit the application
    val appId = appContext.getApplicationId
    println("submitting application id" + appId)
    client.submitApplication(appContext)

  }

}

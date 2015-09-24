package com.example.yarn.client

import java.util.Collections

import scala.collection.JavaConverters._
import org.apache.hadoop.fs.{CommonConfigurationKeysPublic, Path}
import org.apache.hadoop.yarn.api.ApplicationConstants
import org.apache.hadoop.yarn.api.records.{Resource, LocalResource, ContainerLaunchContext}
import org.apache.hadoop.yarn.client.api.YarnClient
import org.apache.hadoop.yarn.conf.YarnConfiguration
import org.apache.hadoop.yarn.util.Records

object Main {

	val numberOfInstances = 1

	def main(args: Array[String]) {
		if(args.length < 6){
			println("usage: <rmAddress> <hdfsHost> <amJarPath> <appJarPath> <amMainClass> <appMainClass>")
			return
		}
		println(args.mkString(", "))
		val rmAddress = args(0) //"54.92.87.167"
		val hdfsHost = args(1) //"hdfs://54.92.87.167/"
		val amJarPath = args(2) //"myJars/yarn-App-assembly-1.0.jar"
		val appJarPath = args(3) //"myJars/yarn-App-assembly-1.0.jar"
		val amMainClass = args(4) //" com.example.yarn.app2.ApplicationMaster"
		val appMainClass = args(5) //" com.example.yarn.app2.Main"

		implicit val conf = new YarnConfiguration()

		conf.set(YarnConfiguration.RM_ADDRESS, rmAddress)
		conf.set(CommonConfigurationKeysPublic.FS_DEFAULT_NAME_KEY, hdfsHost)
		conf.set("fs.hdfs.impl", classOf[org.apache.hadoop.hdfs.DistributedFileSystem].getName)
		conf.set("fs.file.impl", classOf[org.apache.hadoop.fs.LocalFileSystem].getName)

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
				" -Xmx256M " + amMainClass +
				"  " + hdfsHost + appJarPath + " " + appMainClass + " " + numberOfInstances + " "+
				" 1>" + ApplicationConstants.LOG_DIR_EXPANSION_VAR + "/stdout" +
				" 2>" + ApplicationConstants.LOG_DIR_EXPANSION_VAR + "/stderr"
		).asJava)


		//add the jar which contains the Application master code to classpath
		val appMasterJar = Records.newRecord(classOf[LocalResource])
		println(hdfsHost + amJarPath)
		Utils.setUpLocalResource(new Path(hdfsHost + amJarPath), appMasterJar)
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

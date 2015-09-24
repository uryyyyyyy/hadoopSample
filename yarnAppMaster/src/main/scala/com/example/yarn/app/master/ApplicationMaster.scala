package com.example.yarn.app.master

import java.util.Collections

import org.apache.hadoop.fs.Path
import org.apache.hadoop.yarn.api.ApplicationConstants
import org.apache.hadoop.yarn.api.records._
import org.apache.hadoop.yarn.client.api.AMRMClient.ContainerRequest
import org.apache.hadoop.yarn.client.api.{AMRMClient, NMClient}
import org.apache.hadoop.yarn.conf.YarnConfiguration
import org.apache.hadoop.yarn.util.Records

import scala.collection.JavaConverters._

object ApplicationMaster {


  def main(args: Array[String]) {
    val jarPath = args(0)
    val mainClass = args(1)
    val n = args(2).toInt

    implicit val conf = new YarnConfiguration()

    // Create a client to talk to the RM
    val rmClient = AMRMClient.createAMRMClient().asInstanceOf[AMRMClient[ContainerRequest]]
    rmClient.init(conf)
    rmClient.start()
    rmClient.registerApplicationMaster("", 0, "")


    //create a client to talk to NM
    val nmClient = NMClient.createNMClient()
    nmClient.init(conf)
    nmClient.start()

    val priority = Records.newRecord(classOf[Priority])
    priority.setPriority(0)

    //resources needed by each container
    val resource = Records.newRecord(classOf[Resource])
    resource.setMemory(128)
    resource.setVirtualCores(1)


    //request for containers
    for ( i <- 1 to n) {
      val containerAsk = new ContainerRequest(resource,null,null,priority)
      println("asking for " +s"$i")
      rmClient.addContainerRequest(containerAsk)
    }

    var responseId = 0
    var completedContainers = 0

    while( completedContainers < n) {

      val appMasterJar = Records.newRecord(classOf[LocalResource])
      Utils.setUpLocalResource(new Path(jarPath),appMasterJar)

      val env = collection.mutable.Map[String,String]()
      Utils.setUpEnv(env)

      val response = rmClient.allocate(responseId+1)
      responseId+=1
      for (container <- response.getAllocatedContainers.asScala) {
        val ctx =
          Records.newRecord(classOf[ContainerLaunchContext])
        ctx.setCommands(
          List(
            "$JAVA_HOME/bin/java" +
              " -Xmx256M " + mainClass + " myArgs" +
              " 1>" + ApplicationConstants.LOG_DIR_EXPANSION_VAR + "/stdout" +
              " 2>" + ApplicationConstants.LOG_DIR_EXPANSION_VAR + "/stderr"
          ).asJava
        )
        ctx.setLocalResources(Collections.singletonMap("helloworld.jar", appMasterJar))
        ctx.setEnvironment(env.asJava)

        System.out.println("Launching container " + container)
        nmClient.startContainer(container, ctx)
      }

      for ( status <- response.getCompletedContainersStatuses.asScala){
        println("completed"+status.getContainerId)
        completedContainers+=1

      }

      Thread.sleep(10000)
    }

    rmClient.unregisterApplicationMaster(
      FinalApplicationStatus.SUCCEEDED, "", "")
  }

}

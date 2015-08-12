package com.example.yarn

import java.io.IOException
import java.util

import com.google.common.collect.Sets
import org.apache.hadoop.yarn.api.records.YarnApplicationState._
import org.apache.hadoop.yarn.api.records.{ApplicationId, ApplicationReport, QueueInfo}
import org.apache.hadoop.yarn.client.api.YarnClient
import org.apache.hadoop.yarn.conf.YarnConfiguration
import org.apache.hadoop.yarn.exceptions.YarnException

import scala.collection.JavaConversions._

object Util {

	private val sparkBin = "/media/shiba/backup/develop/libraries/spark-1.2.0-bin-hadoop2.4/bin/"
	private val conf = new YarnConfiguration()
	private val yarnConfDir = "/media/shiba/backup/develop/libraries/hadoop-2.5.0/etc/hadoop/"
	private val sparkAssemblyJarPath = "hdfs://192.168.133.214/user/shiba/spark-assembly-1.2.0-hadoop2.4.0.jar"

	def init(): YarnClient = {
		conf.set(YarnConfiguration.RM_ADDRESS, "192.168.133.214")
		val yarnClient = YarnClient.createYarnClient
		yarnClient.init(conf)
		yarnClient.start()
		yarnClient
	}

	def getAllQueue(client:YarnClient):List[QueueInfo] = {
		client.getAllQueues.toList
	}

	def getAllApplicationReport(client:YarnClient):List[ApplicationReport] = {
		getAllQueue(client).flatMap(v => v.getApplications)
	}

	def getApplicationReport(client:YarnClient, id:ApplicationId): ApplicationReport = {
		client.getApplicationReport(id)
	}

	def killYarnApplication(client:YarnClient, appId: ApplicationId):Unit = {
		client.killApplication(appId)
	}

	def getYarnApplicationStatus(client:YarnClient, appId: ApplicationId): Either[String, ApplicationReport] = {
		try{
			val ss = client.getApplicationReport(appId)
			Right(ss)
		}catch{
			case e:YarnException => e.printStackTrace();Left("Yarn Server Error")
			case e:IOException => e.printStackTrace();Left("Network Error")
		}
	}

	def submitSparkAppOnCluster(client:YarnClient, sparkApp:SparkApp): ApplicationId = {
		val command = sparkBin + "spark-submit" +
			s" --class ${sparkApp.entryClass}" +
			" --master yarn-cluster " +
			s" --name ${sparkApp.uniqueCode}" +
			sparkApp.sparkOptions.mkString(" ") +
			s" ${sparkApp.jarFilePath} " +
			sparkApp.appOptions.mkString(" ")

		val r = Runtime.getRuntime
		val envArr = Array("YARN_CONF_DIR=" + yarnConfDir, "SPARK_JAR=" + sparkAssemblyJarPath)
		println("command: " + command)
		println("env: " + envArr.mkString(System.lineSeparator()))
		val process = r.exec(command, envArr)
		loopToGetYarnApplicationId(client, process, sparkApp.uniqueCode)
	}

	def getWaitingYarnApplicationId(client:YarnClient, appName: String):Option[ApplicationId] = {
		val reportList = client.getApplications(Sets.newHashSet("SPARK"), util.EnumSet.of(SUBMITTED, ACCEPTED, RUNNING))
		for (report <- reportList) {
			if (report.getName == appName) {
				return Some(report.getApplicationId)
			}
		}
		None
	}

	def getYarnApplicationId(client:YarnClient, appName: String):Option[ApplicationId] = {
		val reportList = client.getApplications(Sets.newHashSet("SPARK"), util.EnumSet.of(NEW, NEW_SAVING, SUBMITTED, ACCEPTED, RUNNING,FINISHED, FAILED, KILLED))
		for (report <- reportList) {
			if (report.getName == appName) {
				return Some(report.getApplicationId)
			}
		}
		None
	}

	private def loopToGetYarnApplicationId(client:YarnClient, process: Process, appName:String): ApplicationId = {
		var yarnApplicationId: Option[ApplicationId] = None
		while (process.isAlive && yarnApplicationId.isEmpty) {
			Thread.sleep(1000)
			yarnApplicationId = getWaitingYarnApplicationId(client, appName)
		}
		if(yarnApplicationId.isEmpty){
			yarnApplicationId = getYarnApplicationId(client, appName)
		}
		yarnApplicationId.getOrElse(throw new RuntimeException("not found"))
	}

}
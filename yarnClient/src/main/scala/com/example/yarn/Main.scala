package com.example.yarn

object Main {

	val sparkAppPath = "hdfs://52.69.4.230/jars/sparkBatchSample.jar"
	val entryPoint = "com.example.SparkSample"
	val uniqueName = "uniqueName"

	def main(args: Array[String]) {
		println("--start--")
		val client = Util.init()
		val sparkApp = SparkApp(sparkAppPath, entryPoint, uniqueName, List.empty, List.empty)
		val appId = Util.submitSparkAppOnCluster(client, sparkApp)
		println(appId)
	}

}
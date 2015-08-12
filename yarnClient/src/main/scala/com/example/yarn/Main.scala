package com.example.yarn

object Main {

	val sparkAppPath = "hdfs://192.168.133.214/jarFiles/3_1_edp2-spark-batch-sample-2.0.0-SNAPSHOT.jar"
	val entryPoint = "com.worksap.company.edp2.sample.spark.batch.SparkSample"
	val uniqueName = "uniqueName"

	def main(args: Array[String]) {
		println("--start--")
		val client = Util.init()
		val sparkApp = SparkApp(sparkAppPath, entryPoint, uniqueName, List.empty, List.empty)
		val appId = Util.submitSparkAppOnCluster(client, sparkApp)
		println(appId)
	}

}
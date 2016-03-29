package com.github.uryyyyyyy.hadoop.spark.batch.fileIO

import org.apache.spark.{SparkConf, SparkContext}

object Hello {
	def main(args: Array[String]): Unit = {

		val fromFile = args(0) //s3n, s3, hdfs, file
		val toFile = args(1) //s3n, s3, hdfs, file
		val conf = new SparkConf().setAppName("local")
		val sc = new SparkContext(conf)
		val logData = sc.textFile(fromFile)
		logData.saveAsTextFile(toFile)
	}
}

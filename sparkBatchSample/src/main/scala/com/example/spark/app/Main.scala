package com.example.spark.app

import org.apache.spark.{SparkConf, SparkContext}

object Main {
	def main(args: Array[String]) {
		val logFile = args(0) //"hdfs://172.31.3.239/memo2.txt"
		val conf = new SparkConf().setAppName("Simple Application")
		val sc = new SparkContext(conf)
		val logData = sc.textFile(logFile, 2).cache()
		println("----Start----")


		logData.foreach(println)
		val numAs = logData.filter(line => line.contains("a")).count()
		val numBs = logData.filter(line => line.contains("b")).count()
		println("Lines with a: %s, Lines with b: %s".format(numAs, numBs))
	}
}
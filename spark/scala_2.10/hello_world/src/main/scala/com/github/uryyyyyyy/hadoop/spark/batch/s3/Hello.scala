package com.github.uryyyyyyy.hadoop.spark.batch.s3

import org.apache.spark.{SparkConf, SparkContext}

object Hello {
	def main(args: Array[String]): Unit = {

		val logFile = args(0) //s3n://uryyyyyyy/aaa
		val target = args(1) //s3n://uryyyyyyy/aaa
		val conf = new SparkConf().setAppName("s3 app")
		val sc = new SparkContext(conf)
		val logData = sc.textFile(logFile, 2).cache()
		println("----Start----")


		logData.foreach(println)
		val numAs = logData.filter(line => line.contains("a")).count()
		val numBs = logData.filter(line => line.contains("b")).count()
		println("Lines with a: %s, Lines with b: %s".format(numAs, numBs))

		logData.map(line => line.replace("a", "b")).saveAsTextFile(target)
	}
}

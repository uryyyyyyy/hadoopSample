package com.github.uryyyyyyy.hadoop.spark.batch.helloworld

import org.apache.spark.{SparkConf, SparkContext}

object Hello2 {
	def main(args: Array[String]): Unit = {

		val logFile = args(0) //s3n://uryyyyyyy/aaa
		val target = args(1) //s3n://uryyyyyyy/aaa
		val conf = new SparkConf().setAppName("Simple Application")
		val sc = new SparkContext(conf)
		println("----Start----")

		val file = sc.textFile(logFile, 10)
		val f1 = file.flatMap(line => line.split(" "))
		val f2 = f1.map(word => (word, 1))
		val f3 = f2.reduceByKey((a,b) => a + b)
		f3.saveAsTextFile(target)

	}
}

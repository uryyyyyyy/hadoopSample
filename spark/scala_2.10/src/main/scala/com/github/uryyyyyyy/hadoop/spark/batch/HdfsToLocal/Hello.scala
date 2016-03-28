package com.github.uryyyyyyy.hadoop.spark.batch.HdfsToLocal

import org.apache.spark.{SparkConf, SparkContext}

object Hello {
	def main(args: Array[String]): Unit = {

		val localFile = args(0) //s3n://uryyyyyyy/aaa
		val target = args(1) //s3n://uryyyyyyy/aaa
		val conf = new SparkConf().setAppName("local")
		val sc = new SparkContext(conf)
		val logData = sc.textFile("s3n://~~~/tmp")
		val ss = logData.collect()
	}
}

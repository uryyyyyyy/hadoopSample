package com.github.uryyyyyyy.hadoop.spark.batch.generator

import org.apache.spark.{SparkConf, SparkContext}
import org.joda.time.format.DateTimeFormat
import org.joda.time.{DateTime, DateTimeConstants}
import scala.util.Random


object Hello {
	def main(args: Array[String]): Unit = {

		val conf = new SparkConf().setAppName("Simple Application").set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
		val sc = new SparkContext(conf)
		println("----Start----")

		val rdd = sc.range(1, 10000000, 1, 10)

		try {
			val jsonRDD = rdd.map(v => {
				s"""{"id": ${v}, "name": "${v}_myName", "description": ${new Random().nextDouble()} }"""
			})

			jsonRDD.saveAsTextFile("/tmp/myobj.json")
		} finally {
			sc.stop
		}
	}
}
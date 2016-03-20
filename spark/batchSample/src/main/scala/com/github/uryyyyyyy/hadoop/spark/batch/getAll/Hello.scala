package com.github.uryyyyyyy.hadoop.spark.batch.getAll

import org.apache.spark.{SparkConf, SparkContext}
import org.joda.time.format.DateTimeFormat
import org.joda.time.{DateTime, DateTimeConstants}


object Hello {
	def main(args: Array[String]): Unit = {

		val conf = new SparkConf().setAppName("Simple Application").set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
		val sc = new SparkContext(conf)
		val rdd = sc.textFile("/tmp/myobj.json", 10)
		println("----Start----")
		val acc = sc.accumulator(0, "action counter")

		try {
			val rdd2 = rdd.map(v => {
				acc += 1
				(v * 2, 2)
			})

			val numOfSunday = rdd2.collect()
			println(numOfSunday)
			println(acc)
		} finally {
			sc.stop
		}
	}
}
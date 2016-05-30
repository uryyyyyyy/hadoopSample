package com.github.uryyyyyyy.hadoop.spark.graphx

import org.apache.spark.{SparkConf, SparkContext}

object Hello {
	def main(args: Array[String]): Unit = {

		val conf = new SparkConf().setAppName("com.github.uryyyyyyy.hadoop.spark.batch.shuffle").set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
		val sc = new SparkContext(conf)
		val rdd = sc.range(1, 100000, 1, 10)
		println("----Start----")

		try {
			val rdd2 = rdd.map(v => {
				val myObj = MyObj(v, "name", "too long string. too long string. too long string. too long string. too long string. too long string. too long string. too long string. too long string. too long string. too long string. too long string. too long string.too long string. too long string. too long string. too long string. too long string. too long string. too long string. too long string. too long string. too long string. too long string. too long string. too long string. too long string. too long string. too long string. too long string. too long string. too long string. too long string. too long string. too long string. too long string. too long string. too long string. too long string. too long string. ")
				(v % 5000, myObj)
			})

			val list = rdd2.reduceByKey((a, sum) => MyObj(a.id + sum.id, "name", "lll"), 5)
			list.foreach(v => println(v))
			Thread.sleep(10000000)
		} finally {
			sc.stop
		}
	}
}

case class MyObj(id:Long, name:String, bigStr: String)
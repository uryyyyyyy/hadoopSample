package com.github.uryyyyyyy.hadoop.spark.batch.broadcast

import org.apache.spark.{SparkConf, SparkContext}


object Hello {
	def main(args: Array[String]): Unit = {

		val conf = new SparkConf().setAppName("com.github.uryyyyyyy.hadoop.spark.batch.broadcast").set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
		val sc = new SparkContext(conf)
		val rdd = sc.range(1, 100, 1, 10)
		println("----Start----")
		val myObj = MyObj(2, "name")
		val gMyObj = sc.broadcast(myObj)

		try {
			val rdd2 = rdd.map(v => (gMyObj.value.id * v, gMyObj.value.name))

			val list = rdd2.collect()
			list.foreach(v => println(v))
		} finally {
			sc.stop
		}
	}
}

case class MyObj(id:Int, name:String)
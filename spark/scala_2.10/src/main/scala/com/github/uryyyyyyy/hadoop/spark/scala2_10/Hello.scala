package com.github.uryyyyyyy.hadoop.spark.scala2_10

import org.apache.hadoop.classification.InterfaceAudience.Public
import org.apache.spark.{SparkConf, SparkContext}


object Hello {
	def main(args: Array[String]): Unit = {

		val conf = new SparkConf().setAppName("Simple Application")
		val sc = new SparkContext(conf)
		val rdd = sc.range(1, 100000, 1, 10)
		println("----Start----")
		val acc = sc.accumulator(0, "action counter")

		try {
			val rdd2 = rdd.map(v => v * 2)
				.map(v => {acc += 1;v})

			val numOfSunday = rdd2.count()
			println(numOfSunday)
			println(acc)
		} finally {
			sc.stop
		}
	}
}

// you should set as public in scala2.10
//class C(private val x: Any) extends AnyVal
class C(val x: Any) extends AnyVal
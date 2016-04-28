package com.github.uryyyyyyy.hadoop.spark.batch.multiFileOutput

import org.apache.spark.{SparkContext, SparkConf}

object Hello {
	def main(args: Array[String]): Unit = {

		val conf = new SparkConf().setAppName("Simple Application")
		val sc = new SparkContext(conf)
		val rdd = sc.range(1, 100000, 1, 10)
		println("----Start----")

		//check for using scala-library_2.11
		//if using 2.10, this method cause Exception.
		println("hello" -> "world")

		rdd.map(i => (i, "hoge:" + i))
			.saveAsHadoopFile("sile:///home/shiba/path", classOf[String], classOf[String], classOf[RDDMultipleTextOutputFormat])
	}
}
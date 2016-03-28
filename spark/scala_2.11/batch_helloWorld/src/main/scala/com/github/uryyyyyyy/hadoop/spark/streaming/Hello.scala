package com.github.uryyyyyyy.hadoop.spark.streaming

import org.apache.log4j.{Level, Logger}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

object Hello {
	def main(args: Array[String]): Unit = {
		Logger.getRootLogger.setLevel(Level.WARN)

		val conf = new SparkConf().setAppName("streaming helloWorld")
		val sc = new SparkContext(conf)
		val ssc = new StreamingContext(sc, Seconds(1))
		println("----Start----")
		val lines = ssc.socketTextStream("localhost", 9999)
		val words = lines.flatMap(_.split(" "))
		val pairs = words.map((_, 1))
		val wordCount = pairs.reduceByKey(_ + _)
		wordCount.saveAsTextFiles("/tmp/ss")

		ssc.start()
		ssc.awaitTermination()
	}
}

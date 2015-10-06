package com.github.uryyyyyyy.hadoop.spark.streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{StreamingContext, Seconds}

object Main {
	def main(args: Array[String]) :Unit = {
		if (args.length < 1) {
			println("usage: <filepath>")
			return
		}

		val logFile = args(0)//"hdfs://172.31.3.239/memo2.txt"
		// Create Stream
		val sparkConf = new SparkConf().setAppName("myApp")
		val ssc = new StreamingContext(sparkConf, Seconds(2))
		val stream = ssc.textFileStream(logFile)

		sys.ShutdownHookThread {
			println("Gracefully stopping Spark Streaming Application")
			ssc.stop(true, true)
			println("Application stopped")
		}

		stream.foreachRDD(v => {
			println("read line start")
			Thread.sleep(1000)
			println(v.take(1).array)
			println("read line done")
		})

		println("\n====================== Start. ======================")
		ssc.start()
		ssc.awaitTermination()
	}

}

package com.github.uryyyyyyy.hadoop.spark.streaming

import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

object Hello {
	def main(args: Array[String]): Unit = {

		val conf = new SparkConf().setAppName("streaming helloWorld")
		val sc = new SparkContext(conf)
		val batchDuration = Seconds(1)
		val ssc = new StreamingContext(sc, batchDuration)
		println("----Start----")
		val stream1 = ssc.socketTextStream("localhost", 56789)
		val stream2 = stream1.flatMap(_.split(" "))
		stream2.saveAsTextFiles("/tmp/spark/streaming1")

		// ストリーミング処理を3秒間実行
		ssc.start()
		try {
			Thread.sleep(3 * 1000)
		} finally {
			ssc.stop(true) // SparkContextもstopする
		}

	}
}

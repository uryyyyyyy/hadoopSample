package com.github.uryyyyyyy.hadoop.spark.batch.counter

import org.apache.spark.{SparkConf, SparkContext}
import org.joda.time.format.DateTimeFormat
import org.joda.time.{DateTime, DateTimeConstants}


object Hello {
	def main(args: Array[String]): Unit = {

		val targetFile = args(0) // /tmp/date.txt => fetch from hdfs://[MasterIP]:8020/tmp/date.txt
		val conf = new SparkConf().setAppName("Simple Application")
		val sc = new SparkContext(conf)
		val textRDD = sc.textFile(targetFile)
		println("----Start----")

		try {
			val dateTimeRDD = textRDD.map(v => {
				val pattern = DateTimeFormat.forPattern("yyyyMMdd")
				DateTime.parse(v, pattern)
			})

			val sundayRDD = dateTimeRDD.filter(v => v.getDayOfWeek == DateTimeConstants.SUNDAY)

			val numOfSunday = sundayRDD.count()
			println(numOfSunday)
		} finally {
			sc.stop
		}
	}
}

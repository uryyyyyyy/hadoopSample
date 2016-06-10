package com.github.uryyyyyyy.hadoop.spark.batch.serializer

import org.apache.spark.{SparkConf, SparkContext}
import org.joda.time.format.DateTimeFormat
import org.joda.time.{DateTime, DateTimeConstants}


object Hello {
	def main(args: Array[String]): Unit = {

		val flag = args(0)

		val conf = new SparkConf().setAppName("Simple Application").set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
		conf.registerKryoClasses(Array(classOf[MyObj]))
		val sc = new SparkContext(conf)
		val rdd = sc.range(1, 100000, 1, 10)
		println("----Start----")

		val myObj = if (flag == "true"){
			println("----true----")
			new MyObj(1, "hoge")
		}else {
			println("----false----")
			new MyObjNot(1, "hoge")
		}



		try {
			val dateTimeRDD = rdd.map(v => {
				myObj.getId() + v
			})

			val numOfSunday = dateTimeRDD.count()
			println(numOfSunday)
		} finally {
			sc.stop
		}
	}
}

case class MyObj(id:Int, name:String) extends GetId {
	override def getId(): Int = id
}
class MyObjNot(id:Int, name:String) extends GetId {
	override def getId(): Int = id
}

trait GetId{
	def getId():Int
}
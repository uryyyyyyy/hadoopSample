package com.github.uryyyyyyy.hadoop.spark.sql

import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}

object Hello {
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("Simple Application")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)

    val rdd = sc.parallelize(Seq(Person("a", 16), Person("b", 17), Person("c", 18)))
    val dataFrame = sqlContext.createDataFrame(rdd)

    dataFrame.registerTempTable("person")
    println(dataFrame.printSchema())
    println(dataFrame.show())
    val dataFrame2 = dataFrame.sqlContext.sql("select name from person where id = 17")
    println(dataFrame2.show())
  }
}

case class Person(name:String, id:Int)

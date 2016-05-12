package com.github.uryyyyyyy.hadoop.spark.batch.fail

import org.apache.spark.{SparkConf, SparkContext}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object DriverFail {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("fail test app")
    val sc = new SparkContext(conf)
    val rdd = sc.parallelize(1 to 10)

    throw new RuntimeException("driver cause fail")

    rdd.foreach(println)
    sc.stop()
  }
}

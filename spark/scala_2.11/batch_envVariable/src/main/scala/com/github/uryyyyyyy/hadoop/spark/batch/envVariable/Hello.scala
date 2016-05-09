package com.github.uryyyyyyy.hadoop.spark.batch.helloworld

import org.apache.spark.{SparkConf, SparkContext}

object Hello {
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("Simple Application")
    val sc = new SparkContext(conf)
    val rdd = sc.range(1, 100000, 1, 10)
    val accessKey = sys.env("AWS_ACCESS_KEY_ID")
    val secretKey = sys.env("AWS_SECRET_ACCESS_KEY")
    println("----Start----")

    //check for using scala-library_2.11
    //if using 2.10, this method cause Exception.
    println("hello" -> "world")

    rdd.map(i => i*2)
      .foreach(i => println(i))
  }
}

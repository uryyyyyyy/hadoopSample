package com.github.uryyyyyyy.hadoop.spark.batch.fail

import org.apache.spark.{SparkConf, SparkContext}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global

object FutureTaskFail {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("fail test app")
    val sc = new SparkContext(conf)
    val rdd = sc.parallelize(1 to 10)
    val f = Future{
      rdd.map(failBy7(_)).foreach(println)
    }
    Await.result(f, Duration.Inf)
    sc.stop()
  }

  def failBy7(n :Int): Int ={
    if(n == 7){
      throw new RuntimeException("Executor cause fail by 7")
    }
    n
  }
}

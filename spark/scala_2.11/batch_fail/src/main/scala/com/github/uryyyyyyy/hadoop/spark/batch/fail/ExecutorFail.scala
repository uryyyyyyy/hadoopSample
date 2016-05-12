package com.github.uryyyyyyy.hadoop.spark.batch.fail

import org.apache.spark.{SparkConf, SparkContext}

object ExecutorFail {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("fail test app")
    val sc = new SparkContext(conf)

    val rdd = sc.parallelize(1 to 10)
    rdd.map(failBy7(_)).foreach(println)
    sc.stop()
  }

  def failBy7(n :Int): Int ={
    if(n == 7){
      throw new RuntimeException("Executor cause fail by 7")
    }
    n
  }
}

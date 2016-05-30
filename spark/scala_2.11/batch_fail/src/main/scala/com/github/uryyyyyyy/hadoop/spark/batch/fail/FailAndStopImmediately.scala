package com.github.uryyyyyyy.hadoop.spark.batch.fail

import java.io.File

import org.apache.spark.{HashPartitioner, SparkConf, SparkContext}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object FailAndStopImmediately {

  def setSparkScheduler(conf: SparkConf) = {
    conf.set("spark.scheduler.mode", "FAIR")
    val xml = getClass.getClassLoader.getResourceAsStream("pool.xml")
    val homeDir = System.getProperty("user.home")
    val f = new File(homeDir + "/.pool.xml")

    val in = scala.io.Source.fromInputStream(xml)
    val out = new java.io.PrintWriter(f)
    try { in.getLines().foreach(out.print) }
    finally { out.close() }
    conf.set("spark.scheduler.allocation.file", homeDir + "/.pool.xml")
  }

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("fail test app")
    setSparkScheduler(conf)
    val sc = new SparkContext(conf)

    val rdd = sc.parallelize(1 to 200)

    val f1 = Future {
      sc.setLocalProperty("spark.scheduler.pool", "backend")
      println(sc.getSchedulingMode)
      rdd.map(failBy7(_))
        .foreach(v => {
        Thread.sleep(100)
        println(v)
      })
    }
    f1.onFailure{case e:Throwable =>
      sc.cancelAllJobs()
      sc.stop()
      throw e
    }
    //main logic
    sc.setLocalProperty("spark.scheduler.pool", "main")
    println(sc.getSchedulingMode)
    val rdd1 = rdd.groupBy(v => v%6).partitionBy(new HashPartitioner(3))

    val f2 = Future {
      sc.setLocalProperty("spark.scheduler.pool", "backend")
      println(sc.getSchedulingMode)
      rdd1.foreach(v => {
        Thread.sleep(10000)
        println(v)
      })
    }

    rdd1.foreach(v => {
      Thread.sleep(10000)
      println
    })

    Await.result(f1, Duration.Inf)
    Await.result(f2, Duration.Inf)
    sc.stop()
  }

  def failBy7(n :Int): Int ={
    if(n == 7){
      throw new RuntimeException("Executor cause fail by 7")
    }
    n
  }
}

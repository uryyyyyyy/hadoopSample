package com.github.uryyyyyyy.hadoop.spark.batch.multiThreadPool

import java.io.File

import org.apache.spark.{SparkConf, SparkContext}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object Hello {

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

    lazy val file1 = "file:///home/shiba/Desktop/spark1"
    lazy val file2 = "file:///home/shiba/Desktop/spark1"

    val conf = new SparkConf().setAppName("Simple Application")
    setSparkScheduler(conf)

    val sc = new SparkContext(conf)
    //sc.setLogLevel("INFO")
    val rdd = sc.range(1, 1000, 1, 100)
    rdd.persist()

    val f1 = Future {
      //backend logic
      sc.setLocalProperty("spark.scheduler.pool", "backend")
      println(sc.getSchedulingMode)
      rdd.map(v => {
        Thread.sleep(100)
        "f3: " + v
      }).saveAsTextFile(file2)

    }
    //main logic
    sc.setLocalProperty("spark.scheduler.pool", "main")
    println(sc.getSchedulingMode)
    rdd.map(v => {
      Thread.sleep(100)
      "f2: " + v
    }).saveAsTextFile(file1)

    Await.ready(f1, Duration.Inf)
    sc.stop()
  }
}

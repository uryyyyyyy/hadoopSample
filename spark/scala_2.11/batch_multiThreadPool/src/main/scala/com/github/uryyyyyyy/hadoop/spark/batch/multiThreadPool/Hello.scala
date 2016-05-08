package com.github.uryyyyyyy.hadoop.spark.batch.multiThreadPool

import org.apache.spark.{SparkConf, SparkContext}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object Hello {

  def main(args: Array[String]): Unit = {

    lazy val file1 = "s3://<bucket>/spark1"
    lazy val file2 = "s3://<bucket>/spark2"

    val conf = new SparkConf().setAppName("Simple Application")
    conf.set("spark.scheduler.allocation.file", "pool.xml")
    val sc = new SparkContext(conf)
    val rdd = sc.range(1, 3000, 1, 5)
    val accessKey = sys.env("AWS_ACCESS_KEY_ID")
    val secretKey = sys.env("AWS_SECRET_ACCESS_KEY")
    lazy val dynamo = DynamoUtils.setupDynamoClientConnection(accessKey, secretKey)

    rdd.persist()

    val f1 = Future{
      //backend logic
      sc.setLocalProperty("spark.scheduler.pool", "backend")
      rdd.map(v => {
        val table = DynamoUtils.getTable(dynamo)

        DynamoUtils.putItem(table, v)
        val item = DynamoUtils.getItem(table, v)
        item.toString
      }).saveAsTextFile(file1)
    }

    val f2 = Future{
      //main (heavy )logic
      sc.setLocalProperty("spark.scheduler.pool", "main")
      rdd.map(v => {
        Thread.sleep(1000)
        println("heavy result :#" + v)
        "heavy result :#" + v
      }).saveAsTextFile(file2)
    }

    Await.ready(f1, Duration.Inf)
    Await.ready(f2, Duration.Inf)
  }
}

package com.github.uryyyyyyy.hadoop.spark.graphframes

import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}
import org.graphframes._

object Hello {
  def main(args: Array[String]): Unit = {

    //from http://www.slideshare.net/knagato/sparkgraphframes
    val conf = new SparkConf().setAppName("Simple Application")
    val sc = new SparkContext(conf)

    val sqlContext = new SQLContext(sc)

    val v = sqlContext.createDataFrame(List(
      (0L, "user", "u1"),
      (1L, "user", "u2"),
      (2L, "item", "i1"),
      (3L, "item", "i2"),
      (4L, "item", "i3"),
      (5L, "item", "i4")
    )).toDF("id", "type", "name")

    val e = sqlContext.createDataFrame(List(
      (0L, 2L, "purchase"),
      (0L, 3L, "purchase"),
      (0L, 4L, "purchase"),
      (1L, 3L, "purchase"),
      (1L, 4L, "purchase"),
      (1L, 5L, "purchase")
    )).toDF("src", "dst", "type")
    val g = GraphFrame(v, e)

    g.find(" (a)-[]->(x); (b)-[]->(x);" +
      " (b)-[]->(y); !(a)-[]->(y)"
    ).groupBy("a.name", "y.name").count().show()
  }
}

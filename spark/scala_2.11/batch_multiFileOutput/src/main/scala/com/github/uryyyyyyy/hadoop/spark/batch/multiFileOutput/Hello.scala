package com.github.uryyyyyyy.hadoop.spark.batch.multiFileOutput

import org.apache.spark.{SparkConf, SparkContext}

object Hello {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("Simple Application")
    val sc = new SparkContext(conf)

    lazy val basePath = "file:///home/shiba/Desktop/spark"
    val rdd = sc.range(1, 1000, 1, 2)
    println("----Start----")

    //check for using scala-library_2.11
    //if using 2.10, this method cause Exception.
    println("hello" -> "world")

    rdd
      .mapPartitionsWithIndex((p, elms) => {
        elms.map(i => ((i%8).toString + "/" + p, "str" + i))
      })
      .saveAsHadoopFile(basePath, classOf[String], classOf[String], classOf[RDDMultipleTextOutputFormat])

    //check the output(does sparkApp can use the outputs)
    sc.textFile(basePath + "/1").foreach(i => println(i))
    sc.textFile(basePath + "/2").foreach(i => println(i))
  }
}

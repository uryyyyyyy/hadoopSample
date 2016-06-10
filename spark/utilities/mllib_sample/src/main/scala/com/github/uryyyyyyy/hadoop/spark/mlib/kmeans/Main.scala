package com.github.uryyyyyyy.hadoop.spark.mlib.kmeans

import org.apache.spark.mllib.clustering.KMeans
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.{SparkConf, SparkContext}

object Main {

  def main(args: Array[String]): Unit = {

    val path = args(0)

    val conf = new SparkConf().setAppName("word2Vector helloWorld")
    val sc = new SparkContext(conf)

    val rdd = sc.textFile(path)
      .map(v => v.split(" ").map(_.toDouble))
      .map(v => Vectors.dense(v))

    val numCluster = 2
    val numIterations = 30

    val clusters = KMeans.train(rdd, numCluster, numIterations)

    println(clusters.k)
    clusters.clusterCenters.foreach(println)

    val sampleData = Vectors.dense(0.3, 0.3, 0.3)
    println(clusters.predict(sampleData))

    val sampleData2 = Vectors.dense(8.1, 8.2, 8.3)
    println(clusters.predict(sampleData2))

    rdd.foreach(v => println(v + " => " + clusters.predict(v)))

    println("WSSSE " + clusters.computeCost(rdd))
  }
}

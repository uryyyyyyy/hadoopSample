package com.github.uryyyyyyy.hadoop.spark.graphx

import org.apache.spark.graphx._
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Hello {
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("Simple Application")
    val sc = new SparkContext(conf)

    val vertexes = Array(
      (3L, ("rxin", "student")),
      (7L, ("jgonzal", "postdoc")),
      (5L, ("franklin", "prof")),
      (2L, ("istoica", "prof"))
    )
    val users: RDD[(VertexId, (String, String))] =
      sc.parallelize(vertexes)

    val edges = Array(
      Edge(3L, 7L, "collab"),
      Edge(5L, 3L, "advisor"),
      Edge(2L, 5L, "colleague"),
      Edge(5L, 7L, "pi")
    )
    // Create an RDD for edges
    val relationships: RDD[Edge[String]] =
      sc.parallelize(edges)
    // Define a default user in case there are relationship with missing user
    val defaultUser = ("John Doe", "Missing")
    // Build the initial Graph
    val graph = Graph(users, relationships, defaultUser)

    val count1 = graph.vertices.filter { case (id, (name, pos)) => pos == "postdoc" }.count
    println(count1)
    val count2 = graph.edges.filter(e => e.srcId > e.dstId).count
    println(count2)
    val count3 = graph.edges.count()
    println(count3)
  }
}
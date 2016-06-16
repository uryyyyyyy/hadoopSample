package com.github.uryyyyyyy.hadoop.spark.mlib.als

import java.util.Date

import com.twitter.finagle.http.Response
import com.twitter.finagle.{Http, Service, http}
import com.twitter.util.{Await, Future}
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel
import org.apache.spark.{SparkConf, SparkContext}

object Main {
  def main(args: Array[String]) {

    val appName = "RandomRatings"
    val conf = new SparkConf().setAppName(s"$appName")
    val sc = new SparkContext(conf)

    SparkUtils.setS3Impl(sc)

    val model = MatrixFactorizationModel.load(sc, s"s3n://XXX/testdata/small")
    model.productFeatures.cache()
    model.userFeatures.cache()

    def execute(userId: Int):String = {
      val start = new Date()
      val recs = model.recommendProducts(userId, 20)
      val list = recs.toList
      val fin = new Date()
      println("take time = " + (fin.getTime - start.getTime))
      list.mkString(", ")
    }

    val service = new Service[http.Request, http.Response] {
      def apply(req: http.Request): Future[http.Response] = {
        val rep = Response()
        rep.setContentString(execute(1))
        Future(rep)
      }
    }
    val server = Http.serve(":8080", service)
    println("Hi! server start at 8080")
    Await.ready(server)
  }
}
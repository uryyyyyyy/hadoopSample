package com.github.uryyyyyyy.hadoop.spark.mlib

import org.apache.spark.mllib.recommendation.{ALS, MatrixFactorizationModel, Rating}
import org.apache.spark.{SparkConf, SparkContext}

object ALSSample {
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("Simple Application")
    val sc = new SparkContext(conf)

    val ratingsRaw = Seq(
      Rating(1, 1, 1.0),
      Rating(1, 2, 1.0),
      Rating(1, 3, 1.0),
      Rating(2, 1, 1.0),
      Rating(2, 2, 1.0)
    )

    // Load and parse the data
    val data = sc.textFile("data/mllib/als/test.data")
    val ratings = sc.parallelize(ratingsRaw)

    // Build the recommendation model using ALS
    val rank = 10
    val numIterations = 10
    val model = ALS.train(ratings, rank, numIterations, 0.01)

    // Evaluate the model on rating data
    val usersProducts = ratings.map { case Rating(user, product, rate) =>
      (user, product)
    }
    val predictions =
      model.predict(usersProducts).map { case Rating(user, product, rate) =>
        ((user, product), rate)
      }
    val ratesAndPreds = ratings.map { case Rating(user, product, rate) =>
      ((user, product), rate)
    }.join(predictions)
    val MSE = ratesAndPreds.map { case ((user, product), (r1, r2)) =>
      val err = r1 - r2
      err * err
    }.mean()
    println("Mean Squared Error = " + MSE)

    // Save and load model
    model.save(sc, "model.sample")
  }
}

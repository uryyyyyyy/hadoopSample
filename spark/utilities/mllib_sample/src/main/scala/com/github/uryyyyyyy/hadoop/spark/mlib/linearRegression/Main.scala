package com.github.uryyyyyyy.hadoop.spark.mlib.linearRegression

import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.mllib.regression.{LabeledPoint,LinearRegressionWithSGD}
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.feature.StandardScaler
import org.apache.spark.mllib.evaluation.RegressionMetrics
import org.apache.spark.sql.functions.udf

case class Weather( date: String,
  day_of_week: String,
  avg_temp: Double,
  rainfall: Double
)

case class Sales(date: String, sales: Double)

object Main {

  def main(args: Array[String]) {

    val weatherFile = args(0)
    val salesFile = args(1)

    val sparkConf = new SparkConf().setAppName("LinearRegressionExample")
    val sc = new SparkContext(sparkConf)
    val sqlContext = new org.apache.spark.sql.SQLContext(sc)
    import sqlContext.implicits._

    // 気候データの読み込みとDataFrame変換
    val weatherCSVRDD = sc.textFile(weatherFile)
    val headerOfWeatherCSVRDD = sc.parallelize(Array(weatherCSVRDD.first))
    val weatherCSVwithoutHeaderRDD = weatherCSVRDD.subtract(headerOfWeatherCSVRDD)
    val weatherDF = weatherCSVwithoutHeaderRDD.map(_.split(",")).
      map(p => Weather(p(0),
        p(1),
        p(2).trim.toDouble,
        p(5).trim.toDouble
      )).toDF()

    // 売上データの読み込みとDataFrame変換
    val salesCSVRDD = sc.textFile(salesFile)
    val headerOfSalesCSVRDD = sc.parallelize(Array(salesCSVRDD.first))
    val salesCSVwithoutHeaderRDD = salesCSVRDD.subtract(headerOfSalesCSVRDD)
    val salesDF = salesCSVwithoutHeaderRDD.map(_.split(",")).map(p => Sales(p(0), p(1).trim.toDouble)).toDF()

    //println(weatherDF.printSchema)   // Spark-Shellの場合にスキーマを確認するために利用する部分であり、
    //println(salesDF.printSchema)     // コメントアウト

    // データの前処理
    val salesAndWeatherDF = salesDF.join(weatherDF, "date")
    val isWeekend = udf((t: String) => if(t.contains("日") || t.contains("土")) 1d else 0d)
    val replacedSalesAndWeatherDF = salesAndWeatherDF.withColumn("weekend", isWeekend(salesAndWeatherDF("day_of_week"))).drop("day_of_week")
    val selectedDataDF = replacedSalesAndWeatherDF.select("sales", "avg_temp", "rainfall", "weekend")
    val labeledPointsRDD = selectedDataDF.map(row => LabeledPoint(row.getDouble(0),
      Vectors.dense(
        row.getDouble(1),
        row.getDouble(2),
        row.getDouble(3)
      )))

    // データの標準化（平均値の調整とスケーリングのそれぞれを個別に有効化、無効化できます）
    //val scaler = new StandardScaler(withMean = true, withStd = true).fit(labeledPointsRDD.map(x => x.features))
    val scaler = new StandardScaler().fit(labeledPointsRDD.map(x => x.features))
    val scaledLabledPointsRDD = labeledPointsRDD.map(x => LabeledPoint(x.label, scaler.transform(x.features)))

    // 線形回帰モデルの作成
    val numIterations = 20
    scaledLabledPointsRDD.cache
    val linearRegressionModel = LinearRegressionWithSGD.train(scaledLabledPointsRDD, numIterations)

    println("weights :" + linearRegressionModel.weights)

    // アルゴリズムに未知データを適用して予測する
    val targetDataVector1 = Vectors.dense(15.0,15.4,1)
    val targetDataVector2 = Vectors.dense(20.0,0,0)

    val targetScaledDataVector1 = scaler.transform(targetDataVector1)
    val targetScaledDataVector2 = scaler.transform(targetDataVector2)

    val result1 = linearRegressionModel.predict(targetScaledDataVector1)
    val result2 = linearRegressionModel.predict(targetScaledDataVector2)

    println("avg_tmp=15.0,rainfall=15.4,weekend=true : sales = " + result1)
    println("avg_tmp=20.0,rainfall=0,weekend=false : sales = " + result2)

    // 入力データを分割し、評価する
    val splitScaledLabeledPointsRDD = scaledLabledPointsRDD.randomSplit(Array(0.6, 0.4), seed = 11L)
    val trainingScaledLabeledPointsRDD = splitScaledLabeledPointsRDD(0).cache()
    val testScaledLabeledPointsRDD = splitScaledLabeledPointsRDD(1)
    val linearRegressionModel2 = LinearRegressionWithSGD.train(trainingScaledLabeledPointsRDD, numIterations)

    val scoreAndLabels = testScaledLabeledPointsRDD.map { point =>
      val score = linearRegressionModel2.predict(point.features)
      (score, point.label)
    }

    val metrics = new RegressionMetrics(scoreAndLabels)
    println("RMSE = "+ metrics.rootMeanSquaredError)

    // 作成したモデルを保存する
    //linearRegressionModel.save(sc,/path/to/modelDir)

  }
}
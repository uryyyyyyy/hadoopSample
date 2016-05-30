package com.github.uryyyyyyy.hadoop.spark.batch.fileIO

import org.apache.spark.{SparkConf, SparkContext}

object Hello {
  def main(args: Array[String]): Unit = {

    val fromFile = args(0) //s3n, s3, s3a, hdfs, file
    val toFile = args(1) //s3n, s3, s3a, hdfs, file
    val conf = new SparkConf().setAppName("local")
    val sc = new SparkContext(conf)

    val accessKey = sys.env("AWS_ACCESS_KEY_ID")
    val secretKey = sys.env("AWS_SECRET_ACCESS_KEY")

    sc.hadoopConfiguration.set("fs.s3n.awsAccessKeyId", accessKey)
    sc.hadoopConfiguration.set("fs.s3n.awsSecretAccessKey", secretKey)
    sc.hadoopConfiguration.set("fs.s3n.impl", "org.apache.hadoop.fs.s3native.NativeS3FileSystem")

    sc.hadoopConfiguration.set("fs.s3a.awsAccessKeyId", accessKey)
    sc.hadoopConfiguration.set("fs.s3a.awsSecretAccessKey", secretKey)
    sc.hadoopConfiguration.set("fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")

    val logDataS3 = sc.textFile(s"s3://$fromFile")
    logDataS3.saveAsTextFile(s"s3://${toFile}_s3")

    val logDataS3n = sc.textFile(s"s3n://$fromFile")
    logDataS3n.saveAsTextFile(s"s3n://${toFile}_s3n")

    val logDataS3a = sc.textFile(s"s3a://$fromFile")
    logDataS3a.saveAsTextFile(s"s3a://${toFile}_s3a")
  }
}

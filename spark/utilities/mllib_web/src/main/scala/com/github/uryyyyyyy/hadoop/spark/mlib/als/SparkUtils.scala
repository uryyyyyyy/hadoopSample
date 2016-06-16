package com.github.uryyyyyyy.hadoop.spark.mlib.als

import org.apache.hadoop.fs.Path
import org.apache.spark.SparkContext

object SparkUtils {

  def setS3Impl(sc: SparkContext): Unit ={
    sc.hadoopConfiguration.set("fs.s3.awsAccessKeyId", sys.env.get("accessKey").get)
    sc.hadoopConfiguration.set("fs.s3.awsSecretAccessKey", sys.env.get("secretKey").get)
    if(sc.hadoopConfiguration.get("fs.s3.impl") == null){
      sc.hadoopConfiguration.set("fs.s3.impl", "org.apache.hadoop.fs.s3.S3FileSystem")
    }

    sc.hadoopConfiguration.set("fs.s3n.awsAccessKeyId", sys.env.get("accessKey").get)
    sc.hadoopConfiguration.set("fs.s3n.awsSecretAccessKey", sys.env.get("secretKey").get)
    if(sc.hadoopConfiguration.get("fs.s3n.impl") == null){
      sc.hadoopConfiguration.set("fs.s3n.impl", "org.apache.hadoop.fs.s3native.NativeS3FileSystem")
    }
  }

  def cleanUpOutput(sc: SparkContext, path: String): Unit = {
    val fsPath = new Path(path)
    val fsOut = fsPath.getFileSystem(sc.hadoopConfiguration)
    if (fsOut.exists(fsPath)) {
      fsOut.delete(fsPath, true)
      println(s"deleted. path: $path")
    }
  }

}

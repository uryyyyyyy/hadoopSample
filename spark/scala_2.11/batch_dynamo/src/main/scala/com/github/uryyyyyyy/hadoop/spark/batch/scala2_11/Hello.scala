package com.github.uryyyyyyy.hadoop.spark.batch.scala2_11

import com.amazonaws.services.dynamodbv2.document.PrimaryKey
import org.apache.spark.{SparkConf, SparkContext}

object Hello {
	def main(args: Array[String]): Unit = {
		val conf = new SparkConf().setAppName("Simple Application")
		val sc = new SparkContext(conf)
		val rdd = sc.range(1, 1000, 1)
		val accessKey = sys.env("AWS_ACCESS_KEY_ID")
		val secretKey = sys.env("AWS_SECRET_ACCESS_KEY")
		val file = args(0)
		lazy val dynamo = DynamoUtils.setupDynamoClientConnection(accessKey, secretKey)

		println("----Start----")
		rdd.map(v => {
			val table = dynamo.getTable("sample")

			val key = new PrimaryKey("id", 1)
			val ss = table.getItem(key)
			ss.toString
		}).saveAsTextFile(file)

	}
}
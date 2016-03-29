package com.github.uryyyyyyy.hadoop.spark.batch.scala2_11

import java.util

import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.{Region, Regions}
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.amazonaws.services.dynamodbv2.document.{DynamoDB, PrimaryKey}
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

object DynamoUtils {

	def setupDynamoClientConnection(accessKey:String, secretKey:String): DynamoDB = {
		val credentials = new BasicAWSCredentials(accessKey,secretKey)
		val client = new AmazonDynamoDBClient(credentials)
		client.setRegion(Region.getRegion(Regions.AP_NORTHEAST_1))
		val dynamoDB = new DynamoDB(client)

		val table = dynamoDB.getTable("sample")

		val expressionAttributeNames = new util.HashMap[String,String]()
		expressionAttributeNames.put("#p", "pageCount")

		val expressionAttributeValues = new util.HashMap[String,Object]()
		val num = 1.asInstanceOf[Object]
		expressionAttributeValues.put(":val", num)

		val outcome = table.updateItem(
			"id", 1,
			"set #p = #p + :val",
			expressionAttributeNames,
			expressionAttributeValues)
		dynamoDB
	}
}
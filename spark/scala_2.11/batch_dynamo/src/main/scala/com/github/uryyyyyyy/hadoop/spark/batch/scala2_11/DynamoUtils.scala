package com.github.uryyyyyyy.hadoop.spark.batch.scala2_11

import java.util

import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.{Region, Regions}
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.amazonaws.services.dynamodbv2.document.DynamoDB

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
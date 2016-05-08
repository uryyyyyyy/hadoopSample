package com.github.uryyyyyyy.hadoop.spark.batch.multiThreadPool

import java.util

import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.{Region, Regions}
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.amazonaws.services.dynamodbv2.document.{DynamoDB, Item, Table}

object DynamoUtils {

  def setupDynamoClientConnection(accessKey:String, secretKey:String): DynamoDB = {
    val credentials = new BasicAWSCredentials(accessKey,secretKey)
    val client = new AmazonDynamoDBClient(credentials)
    client.setRegion(Region.getRegion(Regions.AP_NORTHEAST_1))
    new DynamoDB(client)
  }

  def getTable(dynamoDB :DynamoDB): Table ={
    dynamoDB.getTable("sample")
  }

  def putItem(table :Table, num: Long): Unit ={
    try {
      val item = new Item().withPrimaryKey("Id", num).withString("Name", "name" + num)
      table.putItem(item)
    }catch{
      case _:Exception => Thread.sleep(1000);println("put retry");putItem(table, num)
    }
  }

  def getItem(table :Table, num: Long): Item ={
    try {
      val item = table.getItem("Id", num)
      println(item.toString)
      item
    }catch{
      case _:Exception => Thread.sleep(1000);println("get retry");getItem(table, num)
    }
  }
}
package com.github.uryyyyyyy.hadoop.spark.batch.scala2_11

object Main {

	def main(args: Array[String]) {
		val accessKey = sys.env("AWS_ACCESS_KEY_ID")
		val secretKey = sys.env("AWS_SECRET_ACCESS_KEY")
		val dynamo = DynamoUtils.setupDynamoClientConnection(accessKey, secretKey)


//		val attributeDefinitions= new util.ArrayList[AttributeDefinition]()
//		attributeDefinitions.add(new AttributeDefinition().withAttributeName("Id").withAttributeType("N"));
//
//		val keySchema = new util.ArrayList[KeySchemaElement]()
//		keySchema.add(new KeySchemaElement().withAttributeName("Id").withKeyType(KeyType.HASH));
//
//		val request = new CreateTableRequest()
//			.withTableName("sampleTable")
//			.withKeySchema(keySchema)
//			.withAttributeDefinitions(attributeDefinitions)
//			.withProvisionedThroughput(new ProvisionedThroughput()
//				.withReadCapacityUnits(5L)
//				.withWriteCapacityUnits(6L));
//
//		val table = dynamoDB.createTable(request);
//
//		table.waitForActive();







//		val tables = dynamoDB.listTables()
//		val iterator = tables.iterator()
//
//		println("table start")
//		while (iterator.hasNext()) {
//			val table = iterator.next()
//			println("table dayo")
//			println(table.getTableName())
//		}
		println("table finish")
	}
}

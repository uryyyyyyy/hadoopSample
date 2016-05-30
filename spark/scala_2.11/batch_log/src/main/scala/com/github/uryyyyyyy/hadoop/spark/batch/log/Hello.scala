package com.github.uryyyyyyy.hadoop.spark.batch.log

import java.util.Properties

import org.apache.log4j.PropertyConfigurator
import org.apache.spark.{SparkConf, SparkContext}
import org.slf4j.LoggerFactory

object Hello {

  def main(args: Array[String]): Unit = {

    val props = new Properties()
    props.load(getClass.getClassLoader.getResourceAsStream("myLog4j.properties"))
    PropertyConfigurator.configure(props)

    val driverLogger = LoggerFactory.getLogger(Hello.getClass)

    val conf = new SparkConf().setAppName("Simple Application")
    val sc = new SparkContext(conf)
    val rdd = sc.range(1, 100, 1, 10)
    driverLogger.info("----Start info----")

    lazy val logic = new ExecutorLogic()

    //check for using scala-library_2.11
    //if using 2.10, this method cause Exception.
    val map = "hello" -> "world"
    driverLogger.debug(map.toString())

    rdd.map(i => i*2)
      .foreach(i => logic.log.info(i.toString))
  }
}

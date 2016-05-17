package com.github.uryyyyyyy.hadoop.spark.batch.log

import java.util.Properties

import org.apache.log4j.PropertyConfigurator
import org.slf4j.LoggerFactory

class DriverLogic {

  val props = new Properties()
  props.load(getClass.getClassLoader.getResourceAsStream("myLog4j.properties"))
  println("load properties")
  println(props)
  PropertyConfigurator.configure(props)

  lazy val log = LoggerFactory.getLogger(classOf[DriverLogic])

}

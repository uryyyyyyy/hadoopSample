package com.example.yarn


case class SparkApp(jarFilePath:String,
                    entryClass:String,
                    uniqueCode:String,
                    sparkOptions: List[String],
                    appOptions: List[String])
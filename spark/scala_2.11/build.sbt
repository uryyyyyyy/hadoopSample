name := """spark2.11_samples"""

version := "1.0"

lazy val sparkVersion = "1.6.1"

lazy val commonSettings = Seq(
  organization := "com.github.uryyyyyyy",
  scalaVersion := "2.11.7",
  libraryDependencies ++= Seq(
    "org.apache.spark" %% "spark-core" % sparkVersion % "provided",
    "org.scalatest" %% "scalatest" % "3.0.0-M15" % "test"
  )
)

lazy val batch_helloWorld = (project in file("batch_helloWorld")).
  settings(commonSettings: _*)

lazy val batch_multiFileOutput = (project in file("batch_multiFileOutput")).
  settings(commonSettings: _*)

lazy val batch_otherLibrary = (project in file("batch_otherLibrary")).
  settings(commonSettings: _*)

lazy val batch_envVariable = (project in file("batch_envVariable")).
  settings(commonSettings: _*)

lazy val batch_hdfs = (project in file("batch_hdfs")).
  settings(commonSettings: _*)

lazy val batch_dynamo = (project in file("batch_dynamo")).
  settings(commonSettings: _*)

lazy val batch_fail = (project in file("batch_fail")).
  settings(commonSettings: _*)

lazy val batch_multiThreadPool = (project in file("batch_multiThreadPool")).
  settings(commonSettings: _*)

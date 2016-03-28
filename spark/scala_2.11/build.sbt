name := """spark2.11_samples"""

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
	"joda-time" % "joda-time" % "2.9.2"
)

lazy val sparkVersion = "1.6.1"

lazy val commonSettings = Seq(
	organization := "com.github.uryyyyyyy",
	scalaVersion := "2.11.7",
	libraryDependencies ++= Seq(
		"org.apache.spark" %% "spark-core" % sparkVersion % "provided",
		"org.apache.spark" %% "spark-sql" % sparkVersion % "provided",
		"org.apache.spark" %% "spark-streaming" % sparkVersion % "provided",
		"org.apache.spark" %% "spark-mllib" % sparkVersion % "provided",
		"org.scalatest" %% "scalatest" % "3.0.0-M15" % "test"
	)
)

assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)

lazy val batch_helloWorld = (project in file("batch_helloWorld")).
	settings(commonSettings: _*)

lazy val streaming_helloWorld = (project in file("streaming_helloWorld")).
	settings(commonSettings: _*)

lazy val batch_otherLibrary = (project in file("batch_otherLibrary")).
	settings(commonSettings: _*)

lazy val batch_s3 = (project in file("batch_s3")).
	settings(commonSettings: _*)

lazy val batch_hdfs = (project in file("batch_hdfs")).
	settings(commonSettings: _*)

lazy val batch_dynamo = (project in file("batch_dynamo")).
	settings(commonSettings: _*)

lazy val sql_helloWorld = (project in file("sql_helloWorld")).
	settings(commonSettings: _*)

lazy val mllib_helloWorld = (project in file("mllib_helloWorld")).
	settings(commonSettings: _*)

lazy val graphx_helloWorld = (project in file("graphx_helloWorld")).
	settings(commonSettings: _*)
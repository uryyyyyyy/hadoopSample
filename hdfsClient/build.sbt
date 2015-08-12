
name := """hdfsClient"""

version := "1.0"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
	"org.apache.hadoop" % "hadoop-common" % "2.5.0",
	"org.apache.hadoop" % "hadoop-hdfs" % "2.5.0",
	"org.scalatest" %% "scalatest" % "2.2.4" % "test"
)
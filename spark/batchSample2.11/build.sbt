name := """sparkBatchSample2.11"""

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
	"org.apache.spark" %% "spark-core" % "1.6.1" % "provided",
	"org.apache.spark" %% "spark-sql" % "1.6.1" % "provided",
	"org.apache.spark" %% "spark-streaming" % "1.6.1" % "provided",
	"org.apache.spark" %% "spark-mllib" % "1.6.1" % "provided",
	"joda-time" % "joda-time" % "2.9.2"

)

assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)
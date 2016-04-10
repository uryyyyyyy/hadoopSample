name := """sparkBatchSample"""

version := "1.0"

scalaVersion := "2.10.6"

val sparkVersion = "1.6.1"

libraryDependencies ++= Seq(
	"org.apache.spark" %% "spark-core" % sparkVersion % "provided",
	"org.apache.spark" %% "spark-sql" % sparkVersion % "provided",
	"org.apache.spark" %% "spark-streaming" % sparkVersion % "provided",
	"org.apache.spark" %% "spark-mllib" % sparkVersion % "provided",
	"joda-time" % "joda-time" % "2.9.2"
)

assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)
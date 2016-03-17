name := """sparkBatchSample"""

version := "1.0"

scalaVersion := "2.10.6"

libraryDependencies ++= Seq(
	"org.apache.spark" %% "spark-core" % "1.6.1",
	"org.apache.spark" %% "spark-sql" % "1.6.1",
	"org.apache.spark" %% "spark-streaming" % "1.6.1"
)


name := """sparkStreamingSample"""

version := "1.0"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
	"org.apache.spark" %% "spark-core" % "1.2.2" % "provided",
	"org.apache.spark" %% "spark-streaming" % "1.2.2" % "provided",
	"org.scalatest" %% "scalatest" % "2.2.4" % "test"
)

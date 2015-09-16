name := """yarn-App"""

version := "1.0"

libraryDependencies ++= Seq(
	"org.apache.hadoop" % "hadoop-common" % "2.5.2" % "provided",
	"org.apache.hadoop" % "hadoop-yarn-client" % "2.5.2" % "provided",
	"org.scalatest" %% "scalatest" % "2.2.4" % "test"
)

name := """yarn_client"""

version := "1.0"
libraryDependencies ++= Seq(
	"org.apache.hadoop" % "hadoop-yarn-client" % "2.7.2" % "provided"
)


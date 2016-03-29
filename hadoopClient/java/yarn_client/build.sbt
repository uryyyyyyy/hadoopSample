name := """yarn_client"""

lazy val hadoopVersion = "2.7.2"

version := "1.0"
libraryDependencies ++= Seq(
	"org.apache.hadoop" % "hadoop-yarn-client" % hadoopVersion,
	"org.apache.hadoop" % "hadoop-client" % hadoopVersion
)
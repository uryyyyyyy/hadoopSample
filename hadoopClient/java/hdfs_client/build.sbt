name := """hdfs_client"""

version := "1.0"
libraryDependencies ++= Seq(
	"org.apache.hadoop" % "hadoop-client" % "2.7.2"
)
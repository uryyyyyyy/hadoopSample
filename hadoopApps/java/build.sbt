name := """hadoopApps"""

version := "1.0"

lazy val hadoopVersion = "2.7.2"

lazy val commonSettings = Seq(
	organization := "com.github.uryyyyyyy",
	autoScalaLibrary := false,
	crossPaths := false,
	javacOptions ++= Seq("-source", "1.8", "-target", "1.8"),
	libraryDependencies ++= Seq(
		"org.apache.hadoop" % "hadoop-client" % hadoopVersion % "provided",
		"junit" % "junit" % "4.12"  % "test"
	)
)

lazy val yarn_batch_helloWorld = (project in file("yarn_batch_helloWorld")).
	settings(commonSettings: _*)

lazy val yarn_appMaster = (project in file("yarn_appMaster")).
	settings(commonSettings: _*)

lazy val mapReduce_helloWorld = (project in file("mapReduce_helloWorld")).
	settings(commonSettings: _*)


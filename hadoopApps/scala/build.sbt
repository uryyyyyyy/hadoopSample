name := """hadoopApps"""

version := "1.0"

lazy val commonSettings = Seq(
	organization := "com.github.uryyyyyyy",
	scalaVersion := "2.11.7",
	libraryDependencies ++= Seq(
		"org.scalatest" %% "scalatest" % "3.0.0-M15" % "test"
	)
)

lazy val yarn_batch_helloWorld = (project in file("yarn_batch_helloWorld")).
	settings(commonSettings: _*)

lazy val yarn_appMaster = (project in file("yarn_appMaster")).
	settings(commonSettings: _*)

lazy val mapReduce_helloWorld = (project in file("mapReduce_helloWorld")).
	settings(commonSettings: _*)


name := """hadoopClient"""

version := "1.0"

lazy val commonSettings = Seq(
  organization := "com.github.uryyyyyyy",
  autoScalaLibrary := false,
  crossPaths := false,
  javacOptions ++= Seq("-source", "1.8", "-target", "1.8"),
  libraryDependencies ++= Seq(
    "junit" % "junit" % "4.12"  % "test"
  )
)

lazy val yarn_client = (project in file("yarn_client")).
  settings(commonSettings: _*)

lazy val hdfs_client = (project in file("hdfs_client")).
  settings(commonSettings: _*)


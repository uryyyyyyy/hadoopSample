name := """hadoopClient"""

version := "1.0"

lazy val hadoopVersion = "2.7.2"

lazy val commonSettings = Seq(
  organization := "com.github.uryyyyyyy",
  autoScalaLibrary := false,
  crossPaths := false,
  javacOptions ++= Seq("-source", "1.8", "-target", "1.8"),
  libraryDependencies ++= Seq(
    "org.apache.hadoop" % "hadoop-client" % hadoopVersion,
    "junit" % "junit" % "4.12"  % "test"
  ),

  assemblyMergeStrategy in assembly := {
    case PathList("javax", "servlet", xs @ _*)         => MergeStrategy.first
    case PathList(ps @ _*) if ps.last endsWith ".properties" => MergeStrategy.first
    case PathList(ps @ _*) if ps.last endsWith ".xml" => MergeStrategy.first
    case PathList(ps @ _*) if ps.last endsWith ".types" => MergeStrategy.first
    case PathList(ps @ _*) if ps.last endsWith ".class" => MergeStrategy.first
    case "application.conf"                            => MergeStrategy.concat
    case "unwanted.txt"                                => MergeStrategy.discard
    case x =>
      val oldStrategy = (assemblyMergeStrategy in assembly).value
      oldStrategy(x)
  }
)

lazy val yarn_client = (project in file("yarn_client")).
  settings(commonSettings: _*)

lazy val hdfs_client = (project in file("hdfs_client")).
  settings(commonSettings: _*)


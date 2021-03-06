name := """spark2.11_samples"""

version := "1.0"

lazy val sparkVersion = "1.6.1"

resolvers += "sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases/"

lazy val commonSettings = Seq(
  scalaVersion := "2.11.7",
  organization := "com.github.uryyyyyyy",
  libraryDependencies ++= Seq(
    "org.apache.spark" %% "spark-core" % sparkVersion % "provided",
    "org.apache.spark" %% "spark-core" % sparkVersion % "test",
    "org.scalatest" %% "scalatest" % "3.0.0-M15" % "test"
  ),
  assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)
)

lazy val streaming_sample = (project in file("streaming_sample"))
  .settings(commonSettings: _*)
  .settings(
    name := """spark2.11_streaming_sample""",
    version := "0.1.0",
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-streaming" % sparkVersion % "provided"
    )
  )

lazy val sql_sample = (project in file("sql_sample"))
  .settings(commonSettings: _*)
  .settings(
    name := """spark2.11_sql_sample""",
    version := "0.1.0",
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-sql" % sparkVersion % "provided",
      "org.apache.spark" %% "spark-sql" % sparkVersion % "test"
    )
  )

lazy val mllib_sample = (project in file("mllib_sample"))
  .settings(commonSettings: _*)
  .settings(
    name := """spark2.11_mllib_sample""",
    version := "0.1.0",
    libraryDependencies ++= Seq(
      "com.github.nscala-time" %% "nscala-time" % "1.2.0",
      "org.apache.lucene" % "lucene-kuromoji" % "3.6.2",
      "org.apache.spark" %% "spark-mllib" % sparkVersion % "provided",
      "org.apache.spark" %% "spark-mllib" % sparkVersion % "test"
    )
  )

lazy val mllib_web = (project in file("mllib_web"))
  .settings(commonSettings: _*)
  .settings(
//    scalaVersion := "2.10.6",
    name := """spark2.11_mllib_sample""",
    version := "0.1.0",
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-mllib" % sparkVersion % "provided",
      "com.twitter" %% "finagle-http" % "6.35.0"
    )
  )

lazy val graphx_sample = (project in file("graphx_sample"))
  .settings(commonSettings: _*)
  .settings(
    name := """spark2.11_graphx_sample""",
    version := "0.1.0",
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-graphx" % sparkVersion % "provided"
    )
  )
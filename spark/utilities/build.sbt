name := """spark2.11_samples"""

version := "1.0"

lazy val sparkVersion = "1.6.1"

lazy val commonSettings = Seq(
  organization := "com.github.uryyyyyyy",
  scalaVersion := "2.11.7",
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
      "org.apache.spark" %% "spark-mllib" % sparkVersion % "provided",
      "org.apache.spark" %% "spark-mllib" % sparkVersion % "test"
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

lazy val graphFrame_sample = (project in file("graphFrame_sample"))
  .settings(commonSettings: _*)
  .settings(
    name := """spark2.11_graphFrame_sample""",
    version := "0.1.0",
    unmanagedBase := baseDirectory.value / "graphFrame",
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-sql" % sparkVersion % "provided"
    )
  )
name := """spark_2.10_sample"""

version := "1.0"

scalaVersion := "2.10.6"

//val sparkVersion = "1.6.1"

organization := "com.github.uryyyyyyy"

lazy val commonSettings = Seq(
  sparkVersion := "1.6.1",
  //  libraryDependencies ++= Seq(
  //    "org.apache.spark" %% "spark-core" % sparkVersion % "provided",
  //    "org.apache.spark" %% "spark-core" % sparkVersion % "test",
  //    "org.scalatest" %% "scalatest" % "3.0.0-M15" % "test"
  //  ),
  assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)
)

lazy val graphflame_sample = (project in file("graphflame_sample"))
  .settings(commonSettings: _*)
  .settings(
    name := """spark2.10_graphflame_sample""",
    version := "0.1.0",
    spDependencies += "graphframes/graphframes:0.1.0-spark1.6",
    //    libraryDependencies ++= Seq(
    //      "org.apache.spark" %% "spark-sql" % sparkVersion % "provided"
    //    )
    sparkComponents += "sql"
  )

lazy val hello_world = (project in file("hello_world"))
  .settings(commonSettings: _*)
  .settings(
    name := """spark2.10_hello_world""",
    version := "0.1.0"
  )
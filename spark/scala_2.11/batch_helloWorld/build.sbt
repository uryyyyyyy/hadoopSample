name := """spark2.11_batch_helloWorld"""

version := "1.0"

scalaVersion := "2.11.7"

lazy val sparkVersion = "1.6.1"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion % "test"
)

assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)
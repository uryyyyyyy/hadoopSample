name := """spark2.11_batch_hdfs"""

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
)

assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)
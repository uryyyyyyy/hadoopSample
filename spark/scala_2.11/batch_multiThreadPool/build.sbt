name := """spark2.11_batch_multiThreadPool"""

version := "1.0"

//scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "com.amazonaws" % "aws-java-sdk-dynamodb" % "1.10.64"
)

assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)
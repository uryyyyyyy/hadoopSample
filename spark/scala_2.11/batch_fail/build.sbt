name := """spark2.11_batch_fail"""

version := "1.0"

libraryDependencies ++= Seq(
)

assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)
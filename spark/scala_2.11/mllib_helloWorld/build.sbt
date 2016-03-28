name := """spark2.11_mllib_helloWorld"""

version := "1.0"

lazy val sparkVersion = "1.6.1"

libraryDependencies ++= Seq(
	"org.apache.spark" %% "spark-mllib" % sparkVersion % "provided"
)

assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)
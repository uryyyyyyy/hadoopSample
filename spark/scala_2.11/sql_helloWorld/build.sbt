name := """spark2.11_sql_helloWorld"""

version := "1.0"

lazy val sparkVersion = "1.6.1"

libraryDependencies ++= Seq(
	"org.apache.spark" %% "spark-sql" % sparkVersion % "provided"
)

assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)
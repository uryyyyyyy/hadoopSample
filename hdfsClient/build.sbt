
name := """hdfsClient"""

version := "1.0"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
	("org.apache.hadoop" % "hadoop-client" % "2.5.2")
		.exclude("commons-beanutils", "commons-beanutils-core")
		.exclude("commons-beanutils", "commons-beanutils")
		.exclude("org.apache.hadoop","hadoop-yarn-api")
		.exclude("org.slf4j", "jcl-over-slf4j")
	,
	"org.scalatest" %% "scalatest" % "2.2.4" % "test"
)
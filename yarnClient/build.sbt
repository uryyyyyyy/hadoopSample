
name := """yarnClient"""

version := "1.0"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
	("org.apache.hadoop" % "hadoop-common" % "2.5.2")
		.exclude("commons-beanutils", "commons-beanutils-core")
		.exclude("commons-beanutils", "commons-beanutils")
		.exclude("org.apache.hadoop","hadoop-yarn-api")
		.exclude("org.slf4j", "jcl-over-slf4j")
	,
	"org.apache.hadoop" % "hadoop-yarn-api" % "2.5.2",
	"org.apache.hadoop" % "hadoop-yarn-common" % "2.5.2",
	("org.apache.hadoop" % "hadoop-yarn-client" % "2.5.2")
	,
	"org.scalatest" %% "scalatest" % "2.2.4" % "test"
)

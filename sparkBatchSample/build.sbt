
name := """sparkBatchSample"""

version := "1.0"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
	("org.apache.spark" %% "spark-core" % "1.4.1")
		.exclude("org.apache.spark", "spark-unsafe_2.11")
		.exclude("org.apache.spark", "spark-network-shuffle_2.11")
		.exclude("org.apache.spark", "spark-launcher_2.11")
		.exclude("org.spark-project.spark", "unused")
		.exclude("org.apache.spark", "spark-network-common_2.11")
		.exclude("com.google.guava", "guava")
		.exclude("com.esotericsoftware.minlog", "minlog")
//		.exclude("org.eclipse.jetty.orbit", "javax.activation")
		.exclude("commons-beanutils", "commons-beanutils-core")
		.exclude("commons-beanutils", "commons-beanutils")
		.exclude("org.apache.hadoop","hadoop-yarn-api")
		.exclude("org.slf4j", "jcl-over-slf4j")
//		.exclude("org.eclipse.jetty.orbit", "javax.mail.glassfish")
//		.exclude("org.eclipse.jetty.orbit", "javax.servlet")
	,
	"org.scalatest" %% "scalatest" % "2.2.4" % "test"
)

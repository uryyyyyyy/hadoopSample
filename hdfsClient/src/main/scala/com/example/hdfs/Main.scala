package com.example.hdfs

import java.io.{BufferedInputStream, File, FileInputStream}

object Main {

	def main(args: Array[String]) {

		val in = new BufferedInputStream(new FileInputStream(new File("/home/shiba/Downloads/simple-yarn-app-master/target/simple-yarn-app-1.1.0.jar")))
		val out = HDFSUtil.put("myJars/SparkBatch.jar")
		HDFSUtil.pipe(in, out.right.get)
	}

}
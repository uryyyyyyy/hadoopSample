package com.example.hdfs

import java.io.{BufferedInputStream, File, FileInputStream}

object Main {

	def main(args: Array[String]) {
		HDFSUtil.list("jars/").foreach(println)
		val in = new BufferedInputStream(new FileInputStream(new File("/home/shiba/Desktop/log.log")))
		val out = HDFSUtil.put("jars/log.log_")
		HDFSUtil.pipe(in, out.right.get)
	}

}
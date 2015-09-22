package com.example.hdfs

import java.io.{BufferedInputStream, File, FileInputStream}

object Main {

	def main(args: Array[String]) {

		val in = new BufferedInputStream(new FileInputStream(new File("/home/shiba/.viminfo")))
		val out = HDFSUtil.put("testDir/moke4.txt")
		HDFSUtil.pipe(in, out.right.get)
	}

}
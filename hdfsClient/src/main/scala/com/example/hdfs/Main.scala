package com.example.hdfs

import java.io.{BufferedInputStream, File, FileInputStream}

object Main {

	def main(args: Array[String]) {
		if(args.length < 3) {
			println("usage:java -jar hdfsClient-assembly-1.0.jar <hdfsUrl> <localFile> <hdfsDir>")
			return
		}
		val hdfsUrl = args(0)
		val localFile = args(1)
		val hdfsDir = args(2)

		val fs = HDFSUtil.getFileSystem(hdfsUrl)
		val in = new BufferedInputStream(new FileInputStream(new File(localFile)))
		val out = HDFSUtil.put(hdfsUrl + hdfsDir, fs)
		HDFSUtil.pipe(in, out.right.get)
	}

}
//package com.example.hdfs.client;
//
//import org.apache.hadoop.fs.FileSystem;
//
//public class HDFSUtil {
//
//	public static FileSystem createFileSystem = {
//		try {
//			val conf = new Configuration(false)
//			conf.set(CommonConfigurationKeysPublic.FS_DEFAULT_NAME_KEY, hdfsUrl)
//			conf.setInt(DFSConfigKeys.DFS_REPLICATION_KEY, 1)
//			FileSystem.get(URI.create(hdfsUrl), conf)
//		} catch {
//			case e:IOException => e.printStackTrace(); throw new RuntimeException()
//		}
//	}
//
//	//distPath = jars/log.log5
//	def put(FileSystemdistPath:String):Either[String, BufferedOutputStream] = {
//		val path = new Path(URI.create(hdfsUrl + distPath))
//		if (fs.exists(path)){
//			Left(s"file is already exist. finePath: $distPath")
//		}else{
//			Right(new BufferedOutputStream(fs.create(path)))
//		}
//	}
//
//	//srcPath = jars/log.log5
//	def get(srcPath:String):Either[String, BufferedInputStream] = {
//		val path = new Path(URI.create(hdfsUrl + srcPath))
//		if (!fs.exists(path)){
//			Left(s"file is not exist. finePath: $srcPath")
//		}else{
//			Right(new BufferedInputStream(fs.open(path)))
//		}
//	}
//
//	def pipe(in:BufferedInputStream, out:BufferedOutputStream):Unit = {
//		val buffer: Array[Byte] = new Array[Byte](1024)
//		var bytesRead: Int = 0
//		var times = 0
//		while ( {
//				bytesRead = in.read(buffer)
//				bytesRead
//		} > 0) {
//			out.write(buffer, 0, bytesRead)
//		}
//		in.close()
//		out.close()
//	}
//
//	//srcPath = jars/log.log5
//	def deleteFile(srcPath:String):Unit = {
//		val path = new Path(URI.create(hdfsUrl + srcPath))
//		if (fs.exists(path)){
//			fs.delete(path, false)
//		}
//	}
//
//	//srcPath = jars/
//	def deleteDir(srcPath:String):Unit = {
//		val path = new Path(URI.create(hdfsUrl + srcPath))
//		if (fs.exists(path)){
//			fs.delete(path, true)
//		}
//	}
//
//	//srcPath = jars/
//	def list(srcPath:String):Seq[Path] = {
//		val path = new Path(URI.create(hdfsUrl + srcPath))
//		if (fs.exists(path)){
//			val ss = fs.listStatus(path)
//			ss.map(v => v.getPath).toSeq
//		}else{
//			Seq.empty
//		}
//	}
//
//	//srcPath = jars/
//	def mkDir(srcPath:String):Unit = {
//		val path = new Path(URI.create(hdfsUrl + srcPath))
//		fs.mkdirs(path)
//	}
//}

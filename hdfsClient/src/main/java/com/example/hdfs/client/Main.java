//package com.example.hdfs.client;
//
//import org.apache.hadoop.conf.Configuration;
//import org.apache.hadoop.fs.CommonConfigurationKeysPublic;
//import org.apache.hadoop.fs.FileStatus;
//import org.apache.hadoop.fs.FileSystem;
//import org.apache.hadoop.fs.Path;
//import org.apache.hadoop.hdfs.DFSConfigKeys;
//
//import java.io.BufferedOutputStream;
//import java.io.IOException;
//import java.net.URI;
//
//public class Main {
//
//	public static void main(String[] args) throws IOException {
//
//		String hdfsUrl = "hdfs://54.65.23.49:8020/";
//		String partialListPath = "/testDir";
//		String partialPutPath = "/testDir";
//
//
//		Configuration conf = new Configuration(false);
//		conf.set(CommonConfigurationKeysPublic.FS_DEFAULT_NAME_KEY, hdfsUrl);
//		conf.setInt(DFSConfigKeys.DFS_REPLICATION_KEY, 1);
//		FileSystem fs = FileSystem.get(URI.create(hdfsUrl), conf);
//
//		Path listPath = new Path(URI.create(hdfsUrl + partialListPath));
//		if (fs.exists(listPath)) {
//			for (FileStatus s : fs.listStatus(listPath)) {
//				System.out.println(s.getPath());
//			}
//		}else System.out.println("empty");
//
//
//		Path putPath = new Path(URI.create(hdfsUrl + partialPutPath));
//		if (fs.exists(putPath)){
//			System.out.println("file is already exist. ");
//		}else{
//			new BufferedOutputStream(fs.create(putPath));
//		}
//	}
//}

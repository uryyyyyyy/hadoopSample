package com.github.uryyyyyyy.hadoop.hdfs.client;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.hadoop.fs.FileSystem;


public class Main{

    public static void main(String[] args) throws IOException {
        if(args.length < 3) {
            System.out.println("usage: <hdfsUrl> <localFile> <hdfsDir>");
            return;
        }
        String hdfsUrl = args[0]; // 192.168.133.214
        String localFile = args[1]; // /home/hoge/my.txt
        String hdfsDir = args[2]; // /myTmp/normalBatch-0.1.0.jar

        FileSystem fs = HDFSUtil.getFileSystem(hdfsUrl);
        InputStream in = new BufferedInputStream(new FileInputStream(new File(localFile)));
        OutputStream out = HDFSUtil.put(hdfsDir, fs);
        HDFSUtil.pipe(in, out);
    }
}
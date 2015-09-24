package com.github.uryyyyyyy.hadoop.hdfs.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.CommonConfigurationKeysPublic;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.DFSConfigKeys;

public class HDFSUtil {

    public static FileSystem getFileSystem(String hdfsUrl) throws IOException {
        Configuration conf = new Configuration(false);
        conf.set(CommonConfigurationKeysPublic.FS_DEFAULT_NAME_KEY, hdfsUrl);
        conf.setInt(DFSConfigKeys.DFS_REPLICATION_KEY, 1);
        return FileSystem.get(URI.create(hdfsUrl), conf);
    }

    //distPath = jars/log.log5
    public static OutputStream put(String srcPath, FileSystem fs) throws IOException {
        Path path = new Path(URI.create(srcPath));
        if (fs.exists(path)){
            throw new IOException("file is not exist. finePath: " + srcPath);
        }else{
            return new BufferedOutputStream(fs.create(path));
        }
    }

    //srcPath = jars/log.log5
    public static Optional<InputStream> get(String srcPath, FileSystem fs) throws IOException {
        Path path = new Path(URI.create(srcPath));
        if (!fs.exists(path)){
            System.out.println("file is not exist. finePath: " + srcPath);
            return Optional.empty();
        }else{
            InputStream in = new BufferedInputStream(fs.open(path));
            return Optional.of(in);
        }
    }

    public static void pipe(InputStream input, OutputStream output) throws IOException {
        int DEFAULT_BUFFER_SIZE = 1024 * 4;
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        int size;
        while (-1 != (size = input.read(buffer))) {
            output.write(buffer, 0, size);
        }
        input.close();
        output.close();
    }

    //srcPath = jars/log.log5
    public static void deleteFile(String srcPath, FileSystem fs) throws IOException {
        Path path = new Path(URI.create(srcPath));
        if (fs.exists(path)){
            fs.delete(path, false);
        }
    }

    //srcPath = jars/
    public static void deleteDir(String srcPath, FileSystem fs) throws IOException {
        Path path = new Path(URI.create(srcPath));
        if (fs.exists(path)){
            fs.delete(path, true);
        }
    }

    //srcPath = jars/
    public static List<FileStatus> list(String srcPath, FileSystem fs) throws IOException {
        Path path = new Path(URI.create(srcPath));
        if (fs.exists(path)){
            FileStatus[] ss = fs.listStatus(path);
            return Arrays.asList(ss);
        }else{
            return Collections.emptyList();
        }
    }

    //srcPath = jars/
    public static void mkDir(String srcPath, FileSystem fs) throws IOException {
        Path path = new Path(URI.create(srcPath));
        fs.mkdirs(path);
    }
}

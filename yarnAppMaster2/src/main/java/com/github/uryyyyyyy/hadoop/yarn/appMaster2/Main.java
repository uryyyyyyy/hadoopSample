package com.github.uryyyyyyy.hadoop.yarn.appMaster2;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.yarn.api.records.FinalApplicationStatus;
import org.apache.hadoop.yarn.client.api.AMRMClient;
import org.apache.hadoop.yarn.client.api.AMRMClient.ContainerRequest;
import org.apache.hadoop.yarn.conf.YarnConfiguration;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

public class Main{

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            throw new RuntimeException(
                    "usage: <jarPath> <mainClass> <args> ...");
        }
        String jarPath = args[0];
        String mainClass = args[1];
        String[] containerArgs = Arrays.copyOfRange(args, 2, args.length);

        //System.out.println("jar path {}, main class {}, arguments {}", jarPath, mainClass, Arrays.toString(containerArgs));

        // Initialize clients to ResourceManager and NodeManagers
        Configuration conf = new YarnConfiguration();
        conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());

        AMRMClient<ContainerRequest> rmClient = AMRMClient.createAMRMClient();
        rmClient.init(conf);
        rmClient.start();

        // Register with ResourceManager
        System.out.println("register ApplicationMaster");
        rmClient.registerApplicationMaster("", 0, "");

        FinalApplicationStatus finalApplicationStatus = FinalApplicationStatus.SUCCEEDED;

        String tempFileName = UUID.randomUUID().toString();
        java.nio.file.Path tempFile = Files.createTempFile(tempFileName, ".jar");

        try {
            FileSystem fileSystem = FileSystem.get(conf);

            Path path = new Path(jarPath);
            if (!fileSystem.exists(path)) {
                System.out.println("File does not exists: "+path);
                return;
            }

            Optional<InputStream> jarFileInputStream = getFileFromHdfs(jarPath,
                    fileSystem);

            Files.copy(jarFileInputStream.get(), tempFile,
                    StandardCopyOption.REPLACE_EXISTING);

            URLClassLoader child = new URLClassLoader(
                    new URL[] { tempFile.toUri().toURL() },
                    Main.class.getClassLoader());

            Method mainMethod = child.loadClass(mainClass).getMethod("main", String[].class);

            System.out.println("start to invoke main method");

            mainMethod.invoke(null, new Object[] { containerArgs });

            System.out.println("finish run main method");
        } catch (Exception e) {
            finalApplicationStatus = FinalApplicationStatus.FAILED;

            System.out.println("run yarn application failed");
            throw e;
        } finally {
            System.out.println("delete temp file: "+tempFile);
            try {
                Files.deleteIfExists(tempFile);
            }catch (Exception ignore) {}

            // Un-register with ResourceManager
            rmClient.unregisterApplicationMaster(finalApplicationStatus, "", "");

            System.out.println("finish un-register AppMaster from resource manager");
        }
    }

    public static Optional<InputStream> getFileFromHdfs(String srcPath,
                                                        FileSystem fs) throws IOException {
        Path path = new Path(URI.create(srcPath));
        if (!fs.exists(path)) {
            System.out.println("file is not exist. finePath: "+srcPath);
            return Optional.empty();
        } else {
            InputStream in = new BufferedInputStream(fs.open(path));
            return Optional.of(in);
        }
    }
}
package com.github.uryyyyyyy.hadoop.yarn.client.util;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.yarn.api.ApplicationConstants;
import org.apache.hadoop.yarn.api.records.LocalResource;
import org.apache.hadoop.yarn.api.records.LocalResourceType;
import org.apache.hadoop.yarn.api.records.LocalResourceVisibility;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.hadoop.yarn.util.Apps;
import org.apache.hadoop.yarn.util.ConverterUtils;

public class YARNLaunchUtil {
    public static void setUpLocalResource(Path resourcePath, LocalResource resource, Configuration conf) throws IOException {
        FileStatus jarStat = FileSystem.get(conf).getFileStatus(resourcePath);
        resource.setResource(ConverterUtils.getYarnUrlFromPath(resourcePath));
        resource.setSize(jarStat.getLen());
        resource.setTimestamp(jarStat.getModificationTime());
        resource.setType(LocalResourceType.FILE);
        resource.setVisibility(LocalResourceVisibility.PUBLIC);
    }

    //add the yarn jars to classpath
    public static void setUpEnv(Map<String, String> env, Configuration conf){
        String[] classPath =  conf.getStrings(YarnConfiguration.YARN_APPLICATION_CLASSPATH,YarnConfiguration.DEFAULT_YARN_APPLICATION_CLASSPATH);
        for(String c : classPath){
            Apps.addToEnvironment(env, ApplicationConstants.Environment.CLASSPATH.name(),
                    c.trim());
        }
        Apps.addToEnvironment(env,
                ApplicationConstants.Environment.CLASSPATH.name(),
                ApplicationConstants.Environment.PWD.$() + File.separator + "*");

    }
}

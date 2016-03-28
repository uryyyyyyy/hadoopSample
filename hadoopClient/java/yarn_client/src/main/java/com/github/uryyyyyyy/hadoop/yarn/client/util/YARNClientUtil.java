package com.github.uryyyyyyy.hadoop.yarn.client.util;

import org.apache.hadoop.fs.CommonConfigurationKeysPublic;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.conf.YarnConfiguration;

import java.util.List;

public class YARNClientUtil {

    public static YarnClient createYarnClient(String rmAddress, String hdfsUrl){
        YarnConfiguration conf = new YarnConfiguration();
        conf.set(YarnConfiguration.RM_ADDRESS, rmAddress);
        conf.set(CommonConfigurationKeysPublic.FS_DEFAULT_NAME_KEY, hdfsUrl);
        YarnClient yarnClient = YarnClient.createYarnClient();
        yarnClient.init(conf);
        yarnClient.start();
        return yarnClient;
    }

    public static String argsToStr(String[] args) {
        StringBuilder sb = new StringBuilder();
        for(String arg : args){
            sb.append(arg);
            sb.append(" ");
        }
        return sb.toString();
    }

    public static String listToStr(List<String> args) {
        StringBuilder sb = new StringBuilder();
        for(String arg : args){
            sb.append(arg);
            sb.append(" ");
        }
        return sb.toString();
    }
}

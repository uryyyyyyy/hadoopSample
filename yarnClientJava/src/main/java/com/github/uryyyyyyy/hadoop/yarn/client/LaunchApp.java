package com.github.uryyyyyyy.hadoop.yarn.client;

import java.io.IOException;

import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.exceptions.YarnException;
import org.apache.hadoop.yarn.util.ConverterUtils;

import com.github.uryyyyyyy.hadoop.yarn.client.util.YARNClient;

public class LaunchApp {

    public static void main(String[] args) throws IOException, YarnException {
        if(args.length < 4){
            System.out.println("usage: <rmAddress> <hdfsUrl> <jarPath>");
            return;
        }
        String rmAddress = args[0]; // 192.168.133.214
        String hdfsUrl = args[1]; // hdfs://192.168.133.214
        String jarPath = args[2]; // tmp/java.jar
        String mainClass = args[3]; // "com.github.uryyyyyyy.hadoop.yarn.appMaster.Main"

        YarnClient client = YARNClient.createYarnClient(rmAddress, hdfsUrl);

        ApplicationId id = YARNClient.launchApp(client, hdfsUrl + jarPath, mainClass);

        System.out.println(ConverterUtils.toString(id));
    }
}
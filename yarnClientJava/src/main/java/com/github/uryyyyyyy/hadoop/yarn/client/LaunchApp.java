package com.github.uryyyyyyy.hadoop.yarn.client;

import java.io.IOException;

import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.exceptions.YarnException;
import org.apache.hadoop.yarn.util.ConverterUtils;

import com.github.uryyyyyyy.hadoop.yarn.client.util.YARNClient;

public class LaunchApp {

    public static void main(String[] args) throws IOException, YarnException {
        if(args.length < 5){
            System.out.println("usage: <rmAddress> <hdfsUrl> <appMasterJarPath> <containerJarPath> <mainClass>");
            return;
        }
        String rmAddress = args[0]; // 192.168.133.214
        String hdfsUrl = args[1]; // hdfs://192.168.133.214
        String appMasterJarPath = args[2]; // tmp/amMaster.jar
        String containerJarPath = args[3]; // "tmp/container.jar"
        String appMasterMainClass = args[4]; // "com.github.uryyyyyyy.hadoop.yarn.appMaster.Main"
        String containerMainClass = args[5]; // "com.github.uryyyyyyy.hadoop.yarn.normal.Main"

        YarnClient client = YARNClient.createYarnClient(rmAddress, hdfsUrl);

        ApplicationId id = YARNClient.launchApp(client, hdfsUrl + appMasterJarPath, hdfsUrl + containerJarPath, appMasterMainClass, containerMainClass);

        System.out.println(ConverterUtils.toString(id));
    }
}
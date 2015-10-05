package com.github.uryyyyyyy.hadoop.yarn.client;

import java.io.IOException;
import java.util.Arrays;

import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.exceptions.YarnException;
import org.apache.hadoop.yarn.util.ConverterUtils;

import com.github.uryyyyyyy.hadoop.yarn.client.util.YARNClient;
import com.google.common.collect.ImmutableList;

public class LaunchSparkApp {

    public static void main(String[] args) throws IOException, YarnException, InterruptedException {
        if(args.length < 5){
            System.out.println("usage: <rmAddress> <hdfsUrl> <appMasterJarPath> <containerJarPath> <mainClass> <container args> ...");
            return;
        }
        String rmAddress = args[0]; // 192.168.133.214
        String hdfsUrl = args[1]; // hdfs://192.168.133.214
        String uniqueCode = args[2]; // "papap"
        String jarFilePath = args[3]; // tmp/amMaster.jar
        String entryClass = args[4]; // "com.github.uryyyyyyy.hadoop.yarn.spark.Main"
        String[] containerArgs = Arrays.copyOfRange(args, 5, args.length);

        YarnClient client = YARNClient.createYarnClient(rmAddress, hdfsUrl);

        ApplicationId id = YARNClient.launchSparkApp(client, hdfsUrl + jarFilePath, entryClass, uniqueCode, ImmutableList.of(), containerArgs);

        System.out.println(ConverterUtils.toString(id));
    }
}
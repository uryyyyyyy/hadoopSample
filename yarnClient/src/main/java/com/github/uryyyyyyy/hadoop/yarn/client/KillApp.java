package com.github.uryyyyyyy.hadoop.yarn.client;

import java.io.IOException;

import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.exceptions.YarnException;

import com.github.uryyyyyyy.hadoop.yarn.client.util.YARNClient;

public class KillApp {

    public static void main(String[] args) throws IOException, YarnException {
        if(args.length < 3){
            System.out.println("usage: <rmAddress> <hdfsUrl> <appMasterJarPath> <containerJarPath> <mainClass>");
            return;
        }

        String rmAddress = args[0]; // 192.168.133.214
        String hdfsUrl = args[1]; // hdfs://192.168.133.214
        String applicationId = args[2]; // application_1444101920240_0001

        YarnClient client = YARNClient.createYarnClient(rmAddress, hdfsUrl);

        YARNClient.killApp(client, applicationId);
    }
}

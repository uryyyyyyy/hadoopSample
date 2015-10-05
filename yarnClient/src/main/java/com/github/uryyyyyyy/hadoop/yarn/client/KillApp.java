package com.github.uryyyyyyy.hadoop.yarn.client;

import java.io.IOException;

import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.exceptions.YarnException;

import com.github.uryyyyyyy.hadoop.yarn.client.util.YARNClient;

public class KillApp {

    public static void main(String[] args) throws IOException, YarnException {
        String rmAddress = "192.168.133.214";
        String hdfsUrl = "hdfs://192.168.133.214";
        String applicationId = "application_1441950155020_0078";

        YarnClient client = YARNClient.createYarnClient(rmAddress, hdfsUrl);

        YARNClient.killApp(client, applicationId);
    }
}

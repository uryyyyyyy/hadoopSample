package com.github.uryyyyyyy.hadoop.yarn.client;

import java.io.IOException;

import org.apache.hadoop.yarn.api.records.ApplicationReport;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.exceptions.YarnException;

import com.github.uryyyyyyy.hadoop.yarn.client.util.YARNClient;

public class GetStatus {

    public static void main(String[] args) throws IOException, YarnException {
        if(args.length < 3){
            System.out.println("usage: <rmAddress> <hdfsUrl> <applicationId>");
            return;
        }
        String rmAddress = args[0]; // 192.168.133.214
        String hdfsUrl = args[1]; // hdfs://192.168.133.214
        String applicationId = args[2]; // application_1441950155020_0060

        YarnClient client = YARNClient.createYarnClient(rmAddress, hdfsUrl);

        ApplicationReport report = YARNClient.getApplicationReport(client, applicationId);
        System.out.println(report);
    }
}

package com.github.uryyyyyyy.hadoop.yarn.client;

import com.github.uryyyyyyy.hadoop.yarn.client.util.YARNClientUtil;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.exceptions.YarnException;
import org.apache.hadoop.yarn.util.ConverterUtils;

import java.io.IOException;

public class KillApp {

	public static void main(String[] args) throws IOException, YarnException {
		if(args.length < 3){
			System.out.println("usage: <rmAddress> <hdfsUrl> <appMasterJarPath> <containerJarPath> <mainClass>");
			return;
		}

		String rmAddress = args[0]; // 192.168.133.214
		String hdfsUrl = args[1]; // hdfs://192.168.133.214
		String applicationId = args[2]; // application_1444101920240_0001

		YarnClient client = YARNClientUtil.createYarnClient(rmAddress, hdfsUrl);

		killApp(client, applicationId);
	}

	private static void killApp(YarnClient client, String appId) throws IOException, YarnException {
		client.killApplication(ConverterUtils.toApplicationId(appId));
	}
}

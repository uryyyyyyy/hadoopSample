package com.github.uryyyyyyy.hadoop.yarn.client;

import com.github.uryyyyyyy.hadoop.yarn.client.util.YARNClientUtil;
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.exceptions.YarnException;
import org.apache.hadoop.yarn.util.ConverterUtils;

import java.io.IOException;
import java.util.Arrays;

public class LaunchMapReduce {

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
		String[] containerArgs = Arrays.copyOfRange(args, 6, args.length);

		YarnClient client = YARNClientUtil.createYarnClient(rmAddress, hdfsUrl);

		ApplicationId id = launchMapReduce(client, hdfsUrl + appMasterJarPath, hdfsUrl + containerJarPath, appMasterMainClass, containerMainClass, containerArgs);

		System.out.println(ConverterUtils.toString(id));
	}

	private static ApplicationId launchMapReduce(YarnClient client, String appMasterJarPath, String containerJarPath, String appMasterMainClass, String containerMainClass, String[] containerArgs) throws IOException, YarnException {
		return null;
	}
}
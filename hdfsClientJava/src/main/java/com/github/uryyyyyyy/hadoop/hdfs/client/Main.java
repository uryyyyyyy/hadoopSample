package com.github.uryyyyyyy.hadoop.hdfs.client;

import org.apache.hadoop.yarn.client.api.YarnClient;

public class Main{

	public static void main(String[] args){
		String rmAddress = args[0];

		System.out.println("start");
		YarnClient client = Util.createYarnClient(rmAddress);

		Util.getApplicationReport(client);
	}
}
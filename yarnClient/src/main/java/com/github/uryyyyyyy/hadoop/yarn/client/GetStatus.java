package com.github.uryyyyyyy.hadoop.yarn.client;

import com.github.uryyyyyyy.hadoop.yarn.client.util.YARNClientUtil;
import org.apache.hadoop.yarn.api.records.ApplicationReport;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.exceptions.YarnException;
import org.apache.hadoop.yarn.util.ConverterUtils;

import java.io.IOException;
import java.util.List;

public class GetStatus {

	public static void main(String[] args) throws IOException, YarnException {
		if(args.length < 3){
			System.out.println("usage: <rmAddress> <hdfsUrl> <applicationId>");
			return;
		}
		String rmAddress = args[0]; // 192.168.133.214
		String hdfsUrl = args[1]; // hdfs://192.168.133.214
		String applicationId = args[2]; // application_1441950155020_0060

		YarnClient client = YARNClientUtil.createYarnClient(rmAddress, hdfsUrl);

		ApplicationReport report = getApplicationReport(client, applicationId);
		System.out.println(report);

		List<ApplicationReport> reports = getAllApplicationReport(client);
		System.out.println(reports);
	}

	private static ApplicationReport getApplicationReport(YarnClient client, String targetId) throws IOException, YarnException {
		return client.getApplicationReport(ConverterUtils.toApplicationId(targetId));
	}

	public static List<ApplicationReport> getAllApplicationReport(YarnClient client) throws IOException, YarnException {
		return client.getApplications();
	}
}

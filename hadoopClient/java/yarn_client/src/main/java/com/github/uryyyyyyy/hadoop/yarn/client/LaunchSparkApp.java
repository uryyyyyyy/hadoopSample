package com.github.uryyyyyyy.hadoop.yarn.client;

import com.github.uryyyyyyy.hadoop.yarn.client.util.YARNClientUtil;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.apache.hadoop.yarn.api.records.ApplicationReport;
import org.apache.hadoop.yarn.api.records.YarnApplicationState;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.exceptions.YarnException;
import org.apache.hadoop.yarn.util.ConverterUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

public class LaunchSparkApp {

	private static final String sparkBin = "/media/shiba/backup/develop/libraries/spark-1.4.1-bin-hadoop2.4/bin/";
	private static final String yarnConfDir = "/media/shiba/backup/develop/libraries/hadoop-2.5.2/etc/hadoop/";
	private static final String sparkAssemblyJarPath = "hdfs://192.168.133.214/tmp/spark-assembly-1.4.1-hadoop2.4.0.jar";


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

		YarnClient client = YARNClientUtil.createYarnClient(rmAddress, hdfsUrl);

		ApplicationId id = launchSparkApp(client, hdfsUrl + jarFilePath, entryClass, uniqueCode, ImmutableList.of(), containerArgs);

		System.out.println(ConverterUtils.toString(id));
	}

	private static ApplicationId launchSparkApp(YarnClient client, String jarFilePath, String entryClass, String uniqueCode, List<String> sparkOptions, String[] appArgs) throws IOException, YarnException, InterruptedException {

		String command = sparkBin + "spark-submit" +
				" --class " + entryClass +
				" --master yarn-cluster " +
				" --name " + uniqueCode +
				" " + YARNClientUtil.listToStr(sparkOptions) +
				" " + jarFilePath +
				" " + YARNClientUtil.argsToStr(appArgs);

		Runtime r = Runtime.getRuntime();
		String[] envArr = new String[2];
		envArr[0] = "YARN_CONF_DIR=" + yarnConfDir;
		envArr[1] = "SPARK_JAR=" + sparkAssemblyJarPath;
		System.out.println("command: " + command);
		System.out.println("env: " + envArr[0] + System.lineSeparator() + envArr[1]);
		Process process = r.exec(command, envArr);
		return loopToGetYarnApplicationId(client, process, uniqueCode);
	}

	private static ApplicationId loopToGetYarnApplicationId(YarnClient client, Process process, String appName) throws InterruptedException, IOException, YarnException {
		ApplicationId applicationId = null;
		while (process.isAlive() && (applicationId == null)) {
			Thread.sleep(1000);
			applicationId = getWaitingYarnApplicationId(client, appName);
		}
		if(applicationId == null){
			applicationId = getYarnApplicationId(client, appName);
		}
		return applicationId;
	}

	private static ApplicationId getWaitingYarnApplicationId(YarnClient client, String appName) throws IOException, YarnException {
		List<ApplicationReport> reportList = client.getApplications(ImmutableSet.of("SPARK"), EnumSet.of(YarnApplicationState.SUBMITTED, YarnApplicationState.ACCEPTED, YarnApplicationState.RUNNING));
		for (ApplicationReport report : reportList) {
			if (report.getName().equals(appName)) {
				return report.getApplicationId();
			}
		}
		throw new RuntimeException("cannot find applicationId");
	}

	private static ApplicationId getYarnApplicationId(YarnClient client, String appName) throws IOException, YarnException {
		List<ApplicationReport> reportList = client.getApplications(ImmutableSet.of("SPARK"), EnumSet.of(YarnApplicationState.NEW, YarnApplicationState.NEW_SAVING, YarnApplicationState.SUBMITTED, YarnApplicationState.ACCEPTED, YarnApplicationState.RUNNING, YarnApplicationState.FINISHED, YarnApplicationState.FAILED, YarnApplicationState.KILLED));
		for (ApplicationReport report : reportList) {
			if (report.getName().equals(appName)) {
				return report.getApplicationId();
			}
		}
		throw new RuntimeException("cannot find applicationId");
	}
}
package com.github.uryyyyyyy.hadoop.yarn.client.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.hadoop.fs.CommonConfigurationKeysPublic;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.yarn.api.ApplicationConstants;
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.apache.hadoop.yarn.api.records.ApplicationReport;
import org.apache.hadoop.yarn.api.records.ApplicationSubmissionContext;
import org.apache.hadoop.yarn.api.records.ContainerLaunchContext;
import org.apache.hadoop.yarn.api.records.LocalResource;
import org.apache.hadoop.yarn.api.records.Resource;
import org.apache.hadoop.yarn.api.records.YarnApplicationState;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.client.api.YarnClientApplication;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.hadoop.yarn.exceptions.YarnException;
import org.apache.hadoop.yarn.util.ConverterUtils;
import org.apache.hadoop.yarn.util.Records;

import com.google.common.collect.ImmutableSet;

public class YARNClient {

    private static final String sparkBin = "/media/shiba/backup/develop/libraries/spark-1.4.1-bin-hadoop2.4/bin/";
    private static final String yarnConfDir = "/media/shiba/backup/develop/libraries/hadoop-2.5.2/etc/hadoop/";
    private static final String sparkAssemblyJarPath = "hdfs://192.168.133.214/tmp/spark-assembly-1.4.1-hadoop2.4.0.jar";

    public static YarnClient createYarnClient(String rmAddress, String hdfsUrl){
        YarnConfiguration conf = new YarnConfiguration();
        conf.set(YarnConfiguration.RM_ADDRESS, rmAddress);
        conf.set(CommonConfigurationKeysPublic.FS_DEFAULT_NAME_KEY, hdfsUrl);
        YarnClient yarnClient = YarnClient.createYarnClient();
        yarnClient.init(conf);
        yarnClient.start();
        return yarnClient;
    }

    public static ApplicationId launchApp(YarnClient client, String appMasterJarPath, String containerJarPath, String appMasterMainClass, String containerMainClass) throws IOException, YarnException {
        // application creation
        YarnClientApplication app = client.createApplication();
        ContainerLaunchContext amContainer = Records.newRecord(ContainerLaunchContext.class);
        //application master is a just java program with given commands

        int numberOfInstances = 1;
        List<String> commands = new ArrayList<>();
        commands.add("$JAVA_HOME/bin/java" +
                " -Xmx256M " + appMasterMainClass +
                "  " + containerJarPath +" "+ containerMainClass + " myArg "+
                " 1>" + ApplicationConstants.LOG_DIR_EXPANSION_VAR + "/stdout" +
                " 2>" + ApplicationConstants.LOG_DIR_EXPANSION_VAR + "/stderr");

        amContainer.setCommands(commands);

        //add the jar which contains the Application master code to classpath
        LocalResource appMasterJar = Records.newRecord(LocalResource.class);
        YARNLaunchUtil.setUpLocalResource(new Path(appMasterJarPath), appMasterJar, client.getConfig());
        amContainer.setLocalResources(Collections.singletonMap("helloworld.jar", appMasterJar));

        //setup env to get all yarn and hadoop classes in classpath
        Map<String, String> env = new HashMap<>();
        YARNLaunchUtil.setUpEnv(env, client.getConfig());
        amContainer.setEnvironment(env);

        //specify resource requirements
        Resource resource = Records.newRecord(Resource.class);
        resource.setMemory(300);
        resource.setVirtualCores(1);

        //context to launch
        ApplicationSubmissionContext appContext = app.getApplicationSubmissionContext();
        appContext.setApplicationName("helloworld");
        appContext.setAMContainerSpec(amContainer);
        appContext.setResource(resource);
        appContext.setQueue("default");

        //submit the application
        ApplicationId appId = appContext.getApplicationId();
        System.out.println("submitting application id" + appId);
        client.submitApplication(appContext);
        return appId;
    }

    public static ApplicationId launchSparkApp(YarnClient client, String jarFilePath, String entryClass, String uniqueCode, List<String> sparkOptions, List<String> appOptions) throws IOException, YarnException, InterruptedException {

        String command = sparkBin + "spark-submit" +
                " --class " + entryClass +
                " --master yarn-cluster " +
                " --name " + uniqueCode +
                " " +//sparkOption
                " " + jarFilePath +
                " " + " "; //appOptions

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

    public static ApplicationId launchMapReduce(YarnClient client) throws IOException, YarnException {
        return null;
    }

    public static void killApp(YarnClient client, String appId) throws IOException, YarnException {
        client.killApplication(ConverterUtils.toApplicationId(appId));
    }

    public static List<List<ApplicationReport>> getAllApplicationReport(YarnClient client) throws IOException, YarnException {
        return client.getAllQueues().stream().map(v -> v.getApplications()).collect(Collectors.toList());
    }

    public static ApplicationReport getApplicationReport(YarnClient client, String targetId) throws IOException, YarnException {
        return client.getApplicationReport(ConverterUtils.toApplicationId(targetId));
    }
}

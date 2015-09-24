package com.github.uryyyyyyy.hadoop.yarn.client.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.client.api.YarnClientApplication;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.hadoop.yarn.exceptions.YarnException;
import org.apache.hadoop.yarn.util.ConverterUtils;
import org.apache.hadoop.yarn.util.Records;

public class YARNClient {

    public static YarnClient createYarnClient(String rmAddress, String hdfsUrl){
        YarnConfiguration conf = new YarnConfiguration();
        conf.set(YarnConfiguration.RM_ADDRESS, rmAddress);
        conf.set(CommonConfigurationKeysPublic.FS_DEFAULT_NAME_KEY, hdfsUrl);
        YarnClient yarnClient = YarnClient.createYarnClient();
        yarnClient.init(conf);
        yarnClient.start();
        return yarnClient;
    }

    public static ApplicationId launchApp(YarnClient client, String jarPath, String mainClass) throws IOException, YarnException {
        // application creation
        YarnClientApplication app = client.createApplication();
        ContainerLaunchContext amContainer = Records.newRecord(ContainerLaunchContext.class);
        //application master is a just java program with given commands

        int numberOfInstances = 1;
        List<String> commands = new ArrayList<>();
        commands.add("$JAVA_HOME/bin/java" +
                " -Xmx256M" +
                " com.github.uryyyyyyy.hadoop.yarn.appMaster.Main"+
                "  " + jarPath +" "+ mainClass + " myArg "+
                " 1>" + ApplicationConstants.LOG_DIR_EXPANSION_VAR + "/stdout" +
                " 2>" + ApplicationConstants.LOG_DIR_EXPANSION_VAR + "/stderr");

        amContainer.setCommands(commands);

        //add the jar which contains the Application master code to classpath
        LocalResource appMasterJar = Records.newRecord(LocalResource.class);
        YARNLaunchUtil.setUpLocalResource(new Path(jarPath), appMasterJar, client.getConfig());
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

    public static ApplicationId launchSpackApp(YarnClient client, String jarPath) throws IOException, YarnException {
        return null;
    }

    public static ApplicationId launchMapReduce(YarnClient client, String jarPath) throws IOException, YarnException {
        return null;
    }

    public static List<List<ApplicationReport>> getAllApplicationReport(YarnClient client) throws IOException, YarnException {
        return client.getAllQueues().stream().map(v -> v.getApplications()).collect(Collectors.toList());
    }

    public static ApplicationReport getApplicationReport(YarnClient client, String targetId) throws IOException, YarnException {
        return client.getApplicationReport(ConverterUtils.toApplicationId(targetId));
    }
}

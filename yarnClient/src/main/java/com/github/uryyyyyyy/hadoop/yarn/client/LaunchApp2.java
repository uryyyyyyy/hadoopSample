package com.github.uryyyyyyy.hadoop.yarn.client;

import com.github.uryyyyyyy.hadoop.yarn.client.util.YARNClientUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.yarn.api.ApplicationConstants;
import org.apache.hadoop.yarn.api.records.*;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.client.api.YarnClientApplication;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.hadoop.yarn.exceptions.YarnException;
import org.apache.hadoop.yarn.util.Apps;
import org.apache.hadoop.yarn.util.ConverterUtils;
import org.apache.hadoop.yarn.util.Records;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class LaunchApp2 {

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

        ApplicationId id = launchApp(client, hdfsUrl + appMasterJarPath, hdfsUrl + containerJarPath, appMasterMainClass, containerMainClass, containerArgs, 300);

        System.out.println(ConverterUtils.toString(id));
    }

    public static ApplicationId launchApp(YarnClient client, String appMasterJarPath, String containerJarPath,
                                          String appMasterMainClass, String containerMainClass,
                                          String[] containerArgs, int memory) throws IOException,
            YarnException {
        // application creation
        YarnClientApplication app = client.createApplication();
        ContainerLaunchContext amContainer = Records
                .newRecord(ContainerLaunchContext.class);
        //application master is a just java program with given commands

        String argsStr = YARNClientUtil.argsToStr(containerArgs);
        List<String> commands = new ArrayList<>();

        commands.add("$JAVA_HOME/bin/java" +
                " -Xmx" + memory + "M " + appMasterMainClass +
                "  " + containerJarPath +" "+ containerMainClass + " " + argsStr + "" +
                " 1>" + ApplicationConstants.LOG_DIR_EXPANSION_VAR + "/stdout" +
                " 2>" + ApplicationConstants.LOG_DIR_EXPANSION_VAR + "/stderr");

        amContainer.setCommands(commands);

        //add the jar which contains the Application master code to classpath
        LocalResource appMasterJar = Records.newRecord(LocalResource.class);
        setUpLocalResource(new Path(appMasterJarPath), appMasterJar, client.getConfig());
        amContainer.setLocalResources(Collections.singletonMap("helloworld.jar", appMasterJar));

        //setup env to get all yarn and hadoop classes in classpath
        Map<String, String> env = new HashMap<>();
        setUpEnv(env, client.getConfig());
        amContainer.setEnvironment(env);

        //specify resource requirements
        Resource resource = Records.newRecord(Resource.class);
        resource.setMemory(300); //TODO memory
        resource.setVirtualCores(1);//TODO code

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

    private static void setUpLocalResource(org.apache.hadoop.fs.Path resourcePath, LocalResource resource, Configuration conf) throws IOException {
        FileStatus jarStat = FileSystem.get(conf).getFileStatus(resourcePath);
        resource.setResource(ConverterUtils.getYarnUrlFromPath(resourcePath));
        resource.setSize(jarStat.getLen());
        resource.setTimestamp(jarStat.getModificationTime());
        resource.setType(LocalResourceType.FILE);
        resource.setVisibility(LocalResourceVisibility.PUBLIC);
    }

    //add the yarn jars to classpath
    private static void setUpEnv(Map<String, String> env, Configuration conf){
        String[] classPath =  conf.getStrings(YarnConfiguration.YARN_APPLICATION_CLASSPATH, YarnConfiguration.DEFAULT_YARN_APPLICATION_CLASSPATH);
        for(String c : classPath){
            Apps.addToEnvironment(env, ApplicationConstants.Environment.CLASSPATH.name(),
                    c.trim());
        }
        Apps.addToEnvironment(env,
                ApplicationConstants.Environment.CLASSPATH.name(),
                ApplicationConstants.Environment.PWD.$() + File.separator + "*");
    }
}
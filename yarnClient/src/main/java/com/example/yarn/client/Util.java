//package com.example.yarn.client;
//
//import org.apache.hadoop.yarn.api.ApplicationConstants;
//import org.apache.hadoop.yarn.api.records.ApplicationId;
//import org.apache.hadoop.yarn.api.records.ApplicationReport;
//import org.apache.hadoop.yarn.api.records.ContainerLaunchContext;
//import org.apache.hadoop.yarn.client.api.YarnClient;
//import org.apache.hadoop.yarn.client.api.YarnClientApplication;
//import org.apache.hadoop.yarn.conf.YarnConfiguration;
//import org.apache.hadoop.yarn.exceptions.YarnException;
//import org.apache.hadoop.yarn.util.Records;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class Util {
//
//	public static YarnClient createYarnClient(String rmAddress, String hdfsUrl){
//		YarnConfiguration conf = new YarnConfiguration();
//		conf.set(YarnConfiguration.RM_ADDRESS, rmAddress);
//		conf.set("fs.defaultFS", hdfsUrl);
//		YarnClient yarnClient = YarnClient.createYarnClient();
//		yarnClient.init(conf);
//		yarnClient.start();
//		return yarnClient;
//	}
//
//	public static ApplicationId launchApp(YarnClient client, String jarPath) throws IOException, YarnException {
//		// application creation
//		YarnClientApplication app = client.createApplication();
//		ContainerLaunchContext amContainer = Records.newRecord(ContainerLaunchContext.class);
//		//application master is a just java program with given commands
//
//		int numberOfInstances = 1;
//		List<String> commands = new ArrayList<>();
//		commands.add("$JAVA_HOME/bin/java" +
//				" -Xmx256M" +
//				" com.example.yarn.app2.ApplicationMaster"+
//				"  " + jarPath +"   "+ numberOfInstances + " "+
//				" 1>" + ApplicationConstants.LOG_DIR_EXPANSION_VAR + "/stdout" +
//				" 2>" + ApplicationConstants.LOG_DIR_EXPANSION_VAR + "/stderr");
//
//		amContainer.setCommands(commands);
//
//
//		//add the jar which contains the Application master code to classpath
//		val appMasterJar = Records.newRecord(classOf[LocalResource])
//		Utils.setUpLocalResource(new Path(jarPath), appMasterJar)
//		amContainer.setLocalResources(Collections.singletonMap("helloworld.jar", appMasterJar))
//
//		//setup env to get all yarn and hadoop classes in classpath
//		val env = collection.mutable.Map[String, String]()
//		Utils.setUpEnv(env)
//		amContainer.setEnvironment(env.asJava)
//
//		//specify resource requirements
//		val resource = Records.newRecord(classOf[Resource])
//		resource.setMemory(300)
//		resource.setVirtualCores(1)
//
//		//context to launch
//		val appContext = app.getApplicationSubmissionContext
//		appContext.setApplicationName("helloworld")
//		appContext.setAMContainerSpec(amContainer)
//		appContext.setResource(resource)
//		appContext.setQueue("default")
//
//		//submit the application
//		val appId = appContext.getApplicationId
//		println("submitting application id" + appId)
//		client.submitApplication(appContext)
//	}
//
//	public static List<List<ApplicationReport>> getAllApplicationReport(YarnClient client) throws IOException, YarnException {
//		return client.getAllQueues().stream().map(v -> v.getApplications()).collect(Collectors.toList());
//	}
//
//	public static ApplicationReport getApplicationReport(YarnClient client, ApplicationId id) throws IOException, YarnException {
//		return client.getApplicationReport(id);
//	}
//}

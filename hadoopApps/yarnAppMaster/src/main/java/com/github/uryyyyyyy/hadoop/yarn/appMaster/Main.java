package com.github.uryyyyyyy.hadoop.yarn.appMaster;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.yarn.api.ApplicationConstants;
import org.apache.hadoop.yarn.api.protocolrecords.AllocateResponse;
import org.apache.hadoop.yarn.api.records.Container;
import org.apache.hadoop.yarn.api.records.ContainerLaunchContext;
import org.apache.hadoop.yarn.api.records.ContainerStatus;
import org.apache.hadoop.yarn.api.records.FinalApplicationStatus;
import org.apache.hadoop.yarn.api.records.LocalResource;
import org.apache.hadoop.yarn.api.records.Priority;
import org.apache.hadoop.yarn.api.records.Resource;
import org.apache.hadoop.yarn.client.api.AMRMClient;
import org.apache.hadoop.yarn.client.api.AMRMClient.ContainerRequest;
import org.apache.hadoop.yarn.client.api.NMClient;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.hadoop.yarn.exceptions.YarnException;
import org.apache.hadoop.yarn.util.Records;

public class Main{

    public static void main(String[] args) throws IOException, YarnException, InterruptedException {
        if(args.length < 2){
            throw new RuntimeException("usage: <jarPath> <mainClass> <args> ...");
        }
        String jarPath = args[0];
        System.out.println(jarPath);
        String mainClass = args[1];
        System.out.println(mainClass);
        String[] containerArgs = Arrays.copyOfRange(args, 2, args.length);
        System.out.println(Arrays.toString(containerArgs));

        // Initialize clients to ResourceManager and NodeManagers
        Configuration conf = new YarnConfiguration();
        conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());

        AMRMClient<ContainerRequest> rmClient = AMRMClient.createAMRMClient();
        rmClient.init(conf);
        rmClient.start();

        NMClient nmClient = NMClient.createNMClient();
        nmClient.init(conf);
        nmClient.start();

        // Register with ResourceManager
        System.out.println("registerApplicationMaster 0");
        rmClient.registerApplicationMaster("", 0, "");
        System.out.println("registerApplicationMaster 1");

        // Priority for worker containers - priorities are intra-application
        Priority priority = Records.newRecord(Priority.class);
        priority.setPriority(0);

        // Resource requirements for worker containers
        Resource capability = Records.newRecord(Resource.class);
        capability.setMemory(128);
        capability.setVirtualCores(1);
        int n = 1;

        // Make container requests to ResourceManager
        for (int i = 0; i < n; ++i) {
            ContainerRequest containerAsk = new ContainerRequest(capability, null, null, priority);
            System.out.println("Making res-req " + i);
            rmClient.addContainerRequest(containerAsk);
        }

        // Obtain allocated containers, launch and check for responses
        int responseId = 0;
        int completedContainers = 0;
        while (completedContainers < n) {

            LocalResource appMasterJar = Records.newRecord(LocalResource.class);
            YARNLaunchUtil.setUpLocalResource(new Path(jarPath), appMasterJar, conf);

            Map<String, String> env = new HashMap<>();
            YARNLaunchUtil.setUpEnv(env, conf);

            AllocateResponse response = rmClient.allocate(responseId + 1);
            responseId += 1;
            for (Container container : response.getAllocatedContainers()) {
                ContainerLaunchContext ctx = Records.newRecord(ContainerLaunchContext.class);

                String containerArgsStr = argsToStr(containerArgs);
                List<String> commands = new ArrayList<>();
                String command = "$JAVA_HOME/bin/java" +
                        " -Xmx256M " + mainClass + " " + containerArgsStr +
                        " 1>" + ApplicationConstants.LOG_DIR_EXPANSION_VAR + "/stdout" +
                        " 2>" + ApplicationConstants.LOG_DIR_EXPANSION_VAR + "/stderr";

                System.out.println("Launching command: " + command);

                commands.add(command);
                ctx.setCommands(commands);

                ctx.setLocalResources(Collections.singletonMap("helloworld.jar", appMasterJar));
                ctx.setEnvironment(env);

                System.out.println("Launching container: " + container);
                nmClient.startContainer(container, ctx);
            }

            for(ContainerStatus status : response.getCompletedContainersStatuses()){
                System.out.println("completed" + status.getContainerId());
                completedContainers+=1;
            }
        }
        Thread.sleep(1000);

        // Un-register with ResourceManager
        rmClient.unregisterApplicationMaster(
                FinalApplicationStatus.SUCCEEDED, "", "");
    }

    private static String argsToStr(String[] args) {
        StringBuilder sb = new StringBuilder();
        for(String arg : args){
            sb.append(arg);
            sb.append(" ");
        }
        return sb.toString();
    }
}
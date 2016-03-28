package com.github.uryyyyyyy.hadoop.yarn.client;

import com.google.common.base.Strings;
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.apache.hadoop.yarn.exceptions.YarnException;
import org.apache.hadoop.yarn.util.ConverterUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetLog {
	private final static int YARN_HEAP_SIZE = 128;// MB

	public static void main(String[] args) throws IOException, YarnException {
		if(args.length < 1){
			System.out.println("usage: <applicationId>");
			return;
		}
		String applicationId = args[0]; // application_1441950155020_0060

		List<String> report = getAppLog(applicationId);
		System.out.println(report);
	}

	private static List<String> getAppLog(String yarnApplicationId) {
		if (Strings.isNullOrEmpty(yarnApplicationId)) {
			throw new RuntimeException("appId should not be null");
		}
		Path output = null;
		try {
			output = Files.createTempFile(yarnApplicationId, ".log");
			downloadYarnLogFromCluster(ConverterUtils.toApplicationId(yarnApplicationId), output.toFile());
			return Files.readAllLines(output);
		} catch (IOException e) {
			throw new RuntimeException("get job log failed.", e);
		} finally {
			if (output != null) {
				try {
					Files.deleteIfExists(output);
				} catch (IOException ignored) {
				}
			}
		}
	}

	private static List<String> getYarnLogCommand(ApplicationId yarnApplicationId) {
		final List<String> commands = new ArrayList<>();

		commands.add("yarn");
		commands.add("logs");
		commands.add("-applicationId");
		commands.add(ConverterUtils.toString(yarnApplicationId));

		return commands;
	}

	private static void downloadYarnLogFromCluster(ApplicationId yarnApplicationId, File output) {
		ProcessBuilder processBuilder = new ProcessBuilder(getYarnLogCommand(yarnApplicationId))
				.redirectOutput(output);

		Map<String, String> environment = processBuilder.environment();
		environment.put("YARN_HEAPSIZE", "" + YARN_HEAP_SIZE);

		Process process = null;
		try {
			process = processBuilder.start();
			int exitCode = process.waitFor();
			if (exitCode != 0) {
				throw new RuntimeException("download log from remote yarn cluster failed, yarnApplicationId="
						+ yarnApplicationId);
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (process != null) {
				process.destroyForcibly();
			}
		}
	}
}

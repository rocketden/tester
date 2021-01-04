package com.rocketden.tester.service;

import com.rocketden.tester.dto.RunDto;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

@Service
public class DockerService {

    private final static int TIME_LIMIT = 2;

    public RunDto spawnAndRun(String folder) {
        try {
            // Create and run disposable docker container with the given temp folder
            ProcessBuilder builder = new ProcessBuilder(getRunCommands(folder));
            builder.redirectErrorStream(true);
            Process process = builder.start();

            return captureOutput(process);

        } catch (Exception e) {
            // Error handling
            System.err.println("Failed to create a docker container");
            return null;
        }
    }

    // Given a process, return its status, console output, and error output
    private RunDto captureOutput(Process process) throws IOException, InterruptedException {
        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(process.getInputStream()));

        StringBuilder output = new StringBuilder();
        String s;
        while ((s = stdInput.readLine()) != null) {
            output.append(s).append("\n");
        }

        boolean exitStatus = process.waitFor(TIME_LIMIT, TimeUnit.SECONDS);

        RunDto runDto = new RunDto();
        runDto.setStatus(exitStatus);
        runDto.setOutput(output.toString());

        return runDto;
    }

    private String[] getRunCommands(String folder) {
        String mountPath = String.format("%s:/app/code", folder);
        return new String[] {"docker", "run", "--rm", "-v", mountPath, "-t", "rocketden/tester"};
    }
}

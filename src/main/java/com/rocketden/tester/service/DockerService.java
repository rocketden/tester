package com.rocketden.tester.service;

import com.rocketden.tester.dto.RunDto;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class DockerService {

    public RunDto spawnAndRun(String folder, String language) {
        try {
            // Create and run disposable docker container with the given temp folder
            String[] commands = getRunCommands(folder, language);
            for (String s: commands) {           
                //Do your stuff here
                System.out.println(s); 
            }
            ProcessBuilder builder = new ProcessBuilder(getRunCommands(folder, language));
            builder.redirectErrorStream(true);
            Process process = builder.start();

            return captureOutput(process);

        } catch (Exception e) {
            // Error handling
            System.out.println("An error occurred");
        }

        // Error handling
        return null;
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

        int exitVal = process.waitFor();

        RunDto runDto = new RunDto();
        runDto.setStatus(exitVal);
        runDto.setOutput(output.toString());

        return runDto;
    }

    private String[] getRunCommands(String folder, String language) {
        String mountPath = String.format("%s:/app/code", folder);
        System.out.println("MountPath:" + mountPath);
        return new String[] {"docker", "run", "--rm", "-v", mountPath, "-t", language};
    }
}

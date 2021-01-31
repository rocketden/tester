package com.rocketden.tester.service;

import com.rocketden.tester.dto.ResultDto;
import com.rocketden.tester.dto.RunDto;
import com.rocketden.tester.exception.DockerSetupError;
import com.rocketden.tester.exception.api.ApiException;
import com.rocketden.tester.model.Language;
import com.rocketden.tester.model.problem.Problem;
import com.rocketden.tester.model.problem.ProblemTestCase;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class DockerService {

    private static final int TIME_LIMIT = 2;

    public static final String DELIMITER_TEST_CASE = "###########_TEST_CASE_############";
    public static final String DELIMITER_SUCCESS = "###########_SUCCESS_############";
    public static final String DELIMITER_FAILURE = "###########_FAILURE_############";

    public RunDto spawnAndRun(String folder, Language language, Problem problem) {
        try {
            // Create and run disposable docker container with the given temp folder
            String[] commands = getRunCommands(folder, language);
            ProcessBuilder builder = new ProcessBuilder(commands);
            builder.redirectErrorStream(true);
            Process process = builder.start();

            return captureOutput(process, problem);

        } catch (Exception e) {
            throw new ApiException(DockerSetupError.BUILD_DOCKER_CONTAINER);
        }
    }

    // Given a process, return its status, console output, and error output
    private RunDto captureOutput(Process process, Problem problem) throws IOException, InterruptedException {
        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(process.getInputStream()));

        List<ProblemTestCase> testCases = problem.getTestCases();

        // Create the List of ResultDto objects.
        StringBuilder output = new StringBuilder();
        int testCase = 0;
        String s;
        while ((s = stdInput.readLine()) != null) {
            output.append(s).append("\n");
            if (output.equals(DELIMITER_TEST_CASE)) {
                
            }
        }

        boolean exitStatus = process.waitFor(TIME_LIMIT, TimeUnit.SECONDS);

        RunDto runDto = new RunDto();
        runDto.setStatus(exitStatus);
        runDto.setResults(null);

        // Set the number of correct test cases.
        runDto.setNumCorrect(10);
        runDto.setNumTestCases(testCases.size());

        // Set the output manually, before the runtime is calculated.
        runDto.setRuntime(0L);
        runDto.setStartTime(LocalDateTime.now());

        return runDto;
    }

    private String[] getRunCommands(String folder, Language language) {
        String mountPath = String.format("%s:/code", folder);
        return new String[] {"docker", "run", "--rm", "-v", mountPath, "-t", language.getDockerContainer()};
    }

    // Return whether the user's output is correct.
    private boolean isCorrect(String output, ProblemTestCase testCase) {
        return true;
    }
}

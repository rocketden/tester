package com.rocketden.tester.service;

import com.rocketden.tester.dto.ResultDto;
import com.rocketden.tester.dto.RunDto;
import com.rocketden.tester.exception.DockerSetupError;
import com.rocketden.tester.exception.ParserError;
import com.rocketden.tester.exception.api.ApiException;
import com.rocketden.tester.model.Language;
import com.rocketden.tester.model.OutputSection;
import com.rocketden.tester.model.problem.Problem;
import com.rocketden.tester.model.problem.ProblemTestCase;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
        List<ResultDto> results = new ArrayList<>();
        ResultDto result = new ResultDto();
        StringBuilder output = new StringBuilder();
        OutputSection outputSection = OutputSection.START;
        int numCorrect = 0;
        int testCount = 0;
        String s;
        while ((s = stdInput.readLine()) != null) {
            // Update the output section.
            if (s.equals(DELIMITER_TEST_CASE)) {
                // Update the result as expected or throw error if misformatted.
                if (outputSection == OutputSection.SUCCESS) {
                    String outputStr = output.toString();
                    ProblemTestCase testCase = testCases.get(testCount);

                    // Set the result fields.
                    result.setUserOutput(outputStr);
                    result.setError(null);
                    result.setCorrectOutput(testCase.getOutput());

                    // Set the output correctness.
                    boolean outputCorrect = isOutputCorrect(outputStr, testCase);
                    if (outputCorrect) {
                        numCorrect++;
                    }
                    result.setCorrect(outputCorrect);
                    results.add(result);

                    // Update / clear variables that control loop process.
                    output.setLength(0);
                    testCount++;
                } else if (outputSection == OutputSection.FAILURE) {
                    String outputStr = output.toString();
                    ProblemTestCase testCase = testCases.get(testCount);

                    // Set the result fields.
                    result.setUserOutput(null);
                    result.setError(outputStr);
                    result.setCorrectOutput(testCase.getOutput());
                    result.setCorrect(false);
                    results.add(result);

                    // Update / clear variables that control loop process.
                    output.setLength(0);
                    testCount++;
                } else if (outputSection == OutputSection.TEST_CASE) {
                    throw new ApiException(ParserError.MISFORMATTED_OUTPUT);
                }

                outputSection = OutputSection.TEST_CASE;
            } else if (s.equals(DELIMITER_SUCCESS)) {
                // Update the result as expected or throw error if misformatted.
                if (outputSection != OutputSection.TEST_CASE) {
                    throw new ApiException(ParserError.MISFORMATTED_OUTPUT);
                }

                result.setConsole(output.toString());
                outputSection = OutputSection.SUCCESS;
            } else if (s.equals(DELIMITER_FAILURE)) {
                // Update the result as expected or throw error if misformatted.
                if (outputSection != OutputSection.TEST_CASE) {
                    throw new ApiException(ParserError.MISFORMATTED_OUTPUT);
                }

                result.setConsole(output.toString());
                outputSection = OutputSection.FAILURE;
            } else {
                // Append new line to output, if line is not a delimiter.
                output.append(s).append("\n");
            }
        }

        boolean exitStatus = process.waitFor(TIME_LIMIT, TimeUnit.SECONDS);

        RunDto runDto = new RunDto();
        runDto.setStatus(exitStatus);
        runDto.setResults(results);

        // Set the number of correct test cases.
        runDto.setNumCorrect(numCorrect);
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
    private boolean isOutputCorrect(String output, ProblemTestCase testCase) {
        return output.equals(testCase.getOutput());
    }
}

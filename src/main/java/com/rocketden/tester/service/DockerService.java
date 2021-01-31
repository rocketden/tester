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

            return parseCaptureOutput(process, problem);

        } catch (Exception e) {
            throw new ApiException(DockerSetupError.BUILD_DOCKER_CONTAINER);
        }
    }

    /**
     * Given a process and the relevant problem, parse and produce
     * the relevant RunDto object to capture the console, solution, and
     * errors for each test case, as well as the correctness and runtime.
     * 
     * @param process The process used to get the testing output.
     * @param problem The current problem associated with this output.
     * @return The RunDto object produced from the output.
     */
    private RunDto parseCaptureOutput(Process process, Problem problem) throws IOException, InterruptedException {
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
                testCount = parseTestCaseOutput(outputSection,
                    output.toString(), results, result, testCases, testCount);
                output.setLength(0);
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

        // Throw error if more tests were captured than exist.
        if (testCount != testCases.size()) {
            throw new ApiException(ParserError.MISFORMATTED_OUTPUT);
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

    /**
     * Handle the update to parse the test case output.
     * 
     * @param outputSection The current output section in the parsing process.
     * @param outputStr The current output, in String form.
     * @param results The list of results, which this method adds to.
     * @param result The current result being built, and possibly added to
     * the results object.
     * @param testCases The list of test cases for this problem.
     * @param testCount The current test number that the parser has reached.
     * 
     * @return The updated testCount number, since this is not a
     * pass-by-reference object and needs to be updated in the original
     * method.
     */
    private Integer parseTestCaseOutput(OutputSection outputSection,
        String outputStr, List<ResultDto> results, ResultDto result,
        List<ProblemTestCase> testCases, Integer testCount) {
        // Update the result as expected or throw error if misformatted.
        if (outputSection == OutputSection.SUCCESS) {
            ProblemTestCase testCase = testCases.get(testCount);

            // Set the result fields.
            result.setUserOutput(outputStr);
            result.setError(null);
            result.setCorrectOutput(testCase.getOutput());

            // Set the output correctness.
            boolean outputCorrect = isOutputCorrect(outputStr, testCase);
            result.setCorrect(outputCorrect);
            results.add(result);

            // Update / clear variables that control loop process.
            testCount++;
        } else if (outputSection == OutputSection.FAILURE) {
            ProblemTestCase testCase = testCases.get(testCount);

            // Set the result fields.
            result.setUserOutput(null);
            result.setError(outputStr);
            result.setCorrectOutput(testCase.getOutput());
            result.setCorrect(false);
            results.add(result);

            // Update / clear variables that control loop process.
            testCount++;
        } else if (outputSection == OutputSection.TEST_CASE) {
            throw new ApiException(ParserError.MISFORMATTED_OUTPUT);
        }

        return testCount;
    }
}

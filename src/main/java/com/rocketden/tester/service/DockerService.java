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
import com.rocketden.tester.service.parsers.OutputParser;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class DockerService {

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
    private RunDto parseCaptureOutput(Process process, Problem problem) throws IOException {
        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(process.getInputStream()));

        List<ProblemTestCase> testCases = problem.getTestCases();

        // Create the List of ResultDto objects.
        List<ResultDto> results = new ArrayList<>();
        ResultDto result = new ResultDto();
        StringBuilder output = new StringBuilder();
        OutputSection outputSection = OutputSection.START;
        int numCorrect = 0;
        String s;
        while ((s = stdInput.readLine()) != null) {
            // Update the output section.
            if (s.equals(OutputParser.DELIMITER_TEST_CASE)) {
                parseTestCaseOutput(outputSection,
                    output.toString(), results, result,
                    testCases.get(results.size()));
                output.setLength(0);
                outputSection = OutputSection.TEST_CASE;
            } else if (s.equals(OutputParser.DELIMITER_SUCCESS)) {
                // Update the result as expected or throw error if misformatted.
                if (outputSection != OutputSection.TEST_CASE) {
                    throw new ApiException(ParserError.MISFORMATTED_OUTPUT);
                }

                result.setConsole(output.toString());
                outputSection = OutputSection.SUCCESS;
            } else if (s.equals(OutputParser.DELIMITER_FAILURE)) {
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
        if (results.size() != testCases.size()) {
            throw new ApiException(ParserError.MISFORMATTED_OUTPUT);
        }

        RunDto runDto = new RunDto();
        runDto.setResults(results);

        // Set the number of correct test cases.
        runDto.setNumCorrect(numCorrect);
        runDto.setNumTestCases(testCases.size());

        // Set the output manually, before the runtime is calculated.
        runDto.setRuntime(0L);

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
     * @param testCase The relevant test case for this parsing step.
     */
    private void parseTestCaseOutput(OutputSection outputSection,
        String outputStr, List<ResultDto> results, ResultDto result,
        ProblemTestCase testCase) {
        // Update the result as expected or throw error if misformatted.
        if (outputSection == OutputSection.TEST_CASE) {
            throw new ApiException(ParserError.MISFORMATTED_OUTPUT);
        } else if (outputSection == OutputSection.SUCCESS) {
            // Set the result fields.
            result.setUserOutput(outputStr);
            result.setError(null);
            result.setCorrectOutput(testCase.getOutput());

            // Set the output correctness.
            boolean outputCorrect = isOutputCorrect(outputStr, testCase);
            result.setCorrect(outputCorrect);
            results.add(result);
        } else if (outputSection == OutputSection.FAILURE) {
            // Set the result fields.
            result.setUserOutput(null);
            result.setError(outputStr);
            result.setCorrectOutput(testCase.getOutput());
            result.setCorrect(false);
            results.add(result);
        }
    }
}

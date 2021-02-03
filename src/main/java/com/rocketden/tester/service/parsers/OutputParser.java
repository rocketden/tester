package com.rocketden.tester.service.parsers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.rocketden.tester.dto.ResultDto;
import com.rocketden.tester.dto.RunDto;
import com.rocketden.tester.exception.ParserError;
import com.rocketden.tester.exception.api.ApiException;
import com.rocketden.tester.model.OutputSection;
import com.rocketden.tester.model.problem.Problem;
import com.rocketden.tester.model.problem.ProblemTestCase;

import org.springframework.stereotype.Service;

@Service
public class OutputParser {

    public static final String DELIMITER_TEST_CASE = "###########_TEST_CASE_############";
    public static final String DELIMITER_SUCCESS = "###########_SUCCESS_############";
    public static final String DELIMITER_FAILURE = "###########_FAILURE_############";

    /**
     * Given a process and the relevant problem, parse and produce
     * the relevant RunDto object to capture the console, solution, and
     * errors for each test case, as well as the correctness and runtime.
     * 
     * @param process The process used to get the testing output.
     * @param problem The current problem associated with this output.
     * @return The RunDto object produced from the output.
     */
    public RunDto parseCaptureOutput(Process process, Problem problem) throws IOException {
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
            if (s.equals(DELIMITER_TEST_CASE)) {
                numCorrect = parseTestCaseOutput(outputSection,
                    output.toString(), results, result,
                    testCases.get(results.size()), numCorrect);
                result = new ResultDto();
                output.setLength(0);
                outputSection = OutputSection.TEST_CASE;
            } else if (s.equals(DELIMITER_SUCCESS)) {
                // Update the result as expected or throw error if misformatted.
                if (outputSection != OutputSection.TEST_CASE) {
                    throw new ApiException(ParserError.MISFORMATTED_OUTPUT);
                }

                result.setConsole(output.toString());
                output.setLength(0);
                outputSection = OutputSection.SUCCESS;
            } else if (s.equals(DELIMITER_FAILURE)) {
                // Update the result as expected or throw error if misformatted.
                if (outputSection != OutputSection.TEST_CASE) {
                    throw new ApiException(ParserError.MISFORMATTED_OUTPUT);
                }

                result.setConsole(output.toString());
                output.setLength(0);
                outputSection = OutputSection.FAILURE;
            } else {
                // Append new line to output, if line is not a delimiter.
                output.append(s).append("\n");
            }
        }

        // Add the last remaining test case output, if one exists.
        numCorrect = parseTestCaseOutput(outputSection,
            output.toString(), results, result,
            testCases.get(results.size()), numCorrect);

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

    /**
     * Handle the update to parse the test case output. This is a helper method
     * to parseCaptureOutput.
     * 
     * @param outputSection The current output section in the parsing process.
     * @param outputStr The current output, in String form.
     * @param results The list of results, which this method adds to.
     * @param result The current result being built, and possibly added to
     * the results object.
     * @param testCase The relevant test case for this parsing step.
     * @param numCorrect The number of correct test cases.
     * @return The current number of correct test cases, so far. This is
     * returned because it is a primitive, and its value will not be
     * preserved in the parent method.
     */
    private int parseTestCaseOutput(OutputSection outputSection,
        String outputStr, List<ResultDto> results, ResultDto result,
        ProblemTestCase testCase, int numCorrect) {
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
            if (outputCorrect) {
                numCorrect++;
            }
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

        return numCorrect;
    }

    // Return whether the user's output is correct.
    private boolean isOutputCorrect(String output, ProblemTestCase testCase) {
        return output.equals(testCase.getOutput());
    }
}

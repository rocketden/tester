package com.rocketden.tester.service.parsers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.rocketden.tester.dto.ResultDto;
import com.rocketden.tester.dto.RunDto;
import com.rocketden.tester.exception.ParserError;
import com.rocketden.tester.exception.api.ApiException;
import com.rocketden.tester.model.OutputSection;
import com.rocketden.tester.model.problem.Problem;
import com.rocketden.tester.model.problem.ProblemIOType;
import com.rocketden.tester.model.problem.ProblemTestCase;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class OutputParser {

    private final Gson gson = new Gson();

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
        log.info("Begin capturing and parsing Docker output");

        // StringBuilder to hold the full output for debugging purposes.
        StringBuilder debugger = new StringBuilder();

        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(process.getInputStream()));

        List<ProblemTestCase> testCases = problem.getTestCases();

        // Create the List of ResultDto objects.
        List<ResultDto> results = new ArrayList<>();
        ResultDto result = new ResultDto();
        StringBuilder output = new StringBuilder();
        OutputSection outputSection = OutputSection.START;
        String s;
        while ((s = stdInput.readLine()) != null) {
            debugger.append(s).append("\n");
            // Update the output section.
            if (s.equals(DELIMITER_TEST_CASE)) {
                parseTestCaseOutput(outputSection,
                    output.toString(), results, result,
                    testCases.get(results.size()), problem.getOutputType());
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
        parseTestCaseOutput(outputSection,
            output.toString(), results, result,
            testCases.get(results.size()), problem.getOutputType());

        // Throw error if more tests were captured than exist.
        if (results.size() != testCases.size()) {
            throw new ApiException(ParserError.MISFORMATTED_OUTPUT);
        }

        log.info("Successfully captured and parsed Docker output");

        RunDto runDto = new RunDto();
        runDto.setResults(results);

        // Set the number of correct test cases.
        runDto.setNumCorrect(calculateNumCorrect(results));
        runDto.setNumTestCases(testCases.size());

        // Set the output manually, before the runtime is calculated.
        runDto.setRuntime(0.0);

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
     */
    private void parseTestCaseOutput(OutputSection outputSection,
        String outputStr, List<ResultDto> results, ResultDto result,
        ProblemTestCase testCase, ProblemIOType outputType) {
        // Update the result as expected or throw error if misformatted.
        if (outputSection == OutputSection.TEST_CASE) {
            throw new ApiException(ParserError.MISFORMATTED_OUTPUT);
        } else if (outputSection == OutputSection.SUCCESS) {
            // Set the result fields.
            result.setUserOutput(outputStr);
            result.setError(null);
            result.setCorrectOutput(testCase.getOutput());
            result.setCorrect(isOutputCorrect(outputStr, testCase, outputType));
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

    /**
     * Calculate the number of correct test case results.
     * 
     * @param results The list of results, a result for each test case.
     * @return The number of correct test case results by the user's code.
     */
    private int calculateNumCorrect(List<ResultDto> results) {
        int numCorrect = 0;
        for (ResultDto result : results) {
            if (result.isCorrect()) {
                numCorrect++;
            }
        }
        return numCorrect;
    }

    // Return whether the user's output is correct.
    protected boolean isOutputCorrect(String outputStr, ProblemTestCase testCase, ProblemIOType outputType) {
        // Deserialize the user and correct outputs to compare object equality.
        Object userOutput = parseRawOutputOfGivenType(outputStr, outputType.getClassType());
        Object correctOutput = parseRawOutputOfGivenType(testCase.getOutput(), outputType.getClassType());

        // Check equality between arrays separately from direct object equality.
        if (outputType.getClassType().isArray()) {
            return Arrays.equals((Object[]) userOutput, (Object[]) correctOutput);
        }
        return userOutput.equals(correctOutput);
    }

    // Helper function to parse the output into the given object.
    private Object parseRawOutputOfGivenType(String output, Class<?> classType) {
        try {
            return gson.fromJson(output, classType);
        } catch (Exception e) {
            throw new ApiException(ParserError.INVALID_OUTPUT);
        }
    }
}

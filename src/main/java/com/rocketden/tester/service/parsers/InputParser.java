package com.rocketden.tester.service.parsers;

import com.google.gson.Gson;
import com.rocketden.tester.exception.ParserError;
import com.rocketden.tester.exception.api.ApiException;
import com.rocketden.tester.model.problem.Problem;
import com.rocketden.tester.model.problem.ProblemIOType;
import com.rocketden.tester.model.problem.ProblemInput;
import com.rocketden.tester.model.problem.ProblemTestCase;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class InputParser {

    private final Gson gson = new Gson();

    public List<Object> parseTestCase(Problem problem, ProblemTestCase testCase) {
        String[] rawInputs = splitTestCaseIntoInputs(testCase.getInput());

        if (rawInputs.length != problem.getProblemInputs().size()) {
            throw new ApiException(ParserError.INCORRECT_COUNT);
        }

        List<Object> inputs = new ArrayList<>();
        for (int i = 0; i < rawInputs.length; i++) {
            Object object = gson.fromJson(rawInputs[i], Object.class);
            inputs.add(object);
        }

        return inputs;
    }

    protected String[] splitTestCaseIntoInputs(String input) {
        if (input == null) {
            throw new ApiException(ParserError.INVALID_FORMAT);
        }

        return input.split("\n");
    }

    public static void main(String[] args) {
        InputParser parser = new InputParser();

        ProblemTestCase testCase = new ProblemTestCase();
        testCase.setInput("[1, 2, 3]");

        ProblemInput problemInput = new ProblemInput();
        problemInput.setType(ProblemIOType.ARRAY_INTEGER);

        Problem problem = new Problem();
        problem.setTestCases(Collections.singletonList(testCase));
        problem.setProblemInputs(Collections.singletonList(problemInput));

        List<Object> output = parser.parseTestCase(problem, testCase);
        System.out.println(output);
    }
}

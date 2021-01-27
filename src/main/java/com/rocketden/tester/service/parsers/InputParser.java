package com.rocketden.tester.service.parsers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.rocketden.tester.exception.ParserError;
import com.rocketden.tester.exception.ProblemError;
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
        List<ProblemInput> problemInputs = problem.getProblemInputs();
        String[] rawInputs = splitTestCaseIntoInputs(testCase.getInput());

        if (rawInputs.length != problemInputs.size()) {
            throw new ApiException(ParserError.INCORRECT_COUNT);
        }

        List<Object> inputs = new ArrayList<>();
        for (int i = 0; i < rawInputs.length; i++) {
            ProblemIOType type = problemInputs.get(i).getType();
            Object object = parseRawInputOfGivenType(rawInputs[i], type.getClassType());

            inputs.add(object);
        }

        return inputs;
    }

    private Object parseRawInputOfGivenType(String input, Class<?> classType) {
        try {
            return gson.fromJson(input, classType);
        } catch (JsonSyntaxException e) {
            throw new ApiException(ParserError.INVALID_INPUT);
        }
    }

    protected String[] splitTestCaseIntoInputs(String input) {
        if (input == null) {
            throw new ApiException(ParserError.INVALID_INPUT);
        }

        return input.split("\n");
    }

    public static void main(String[] args) {
        InputParser parser = new InputParser();

        ProblemTestCase testCase = new ProblemTestCase();
        testCase.setInput("4");

        ProblemInput problemInput = new ProblemInput();
        problemInput.setType(ProblemIOType.INTEGER);

        Problem problem = new Problem();
        problem.setTestCases(Collections.singletonList(testCase));
        problem.setProblemInputs(Collections.singletonList(problemInput));

        List<Object> output = parser.parseTestCase(problem, testCase);
        System.out.println(output);
    }
}

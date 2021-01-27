package com.rocketden.tester.service.parsers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.rocketden.tester.exception.ParserError;
import com.rocketden.tester.exception.api.ApiException;
import com.rocketden.tester.model.problem.Problem;
import com.rocketden.tester.model.problem.ProblemIOType;
import com.rocketden.tester.model.problem.ProblemInput;
import com.rocketden.tester.model.problem.ProblemTestCase;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InputParser {

    private final Gson gson = new Gson();

    public List<Object> parseTestCase(Problem problem, ProblemTestCase testCase) {
        // TODO: trim ending new lines
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

    private String[] splitTestCaseIntoInputs(String input) {
        if (input == null) {
            throw new ApiException(ParserError.INVALID_INPUT);
        }

        return input.split("\n");
    }
}

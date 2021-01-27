package com.rocketden.tester.service.parsers;

import com.google.gson.Gson;
import com.rocketden.tester.exception.ParserError;
import com.rocketden.tester.exception.api.ApiException;
import com.rocketden.tester.model.problem.ProblemTestCase;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InputParser {

    private final Gson gson = new Gson();

    // TODO: requires error validation not just for invalid format but e.g. "abc" not a valid char type
    public List<Object> parseTestCase(ProblemTestCase testCase) {
        String[] inputs = splitTestCaseIntoInputs(testCase.getInput());

        return null;
    }

    protected String[] splitTestCaseIntoInputs(String input) {
        if (input == null) {
            throw new ApiException(ParserError.INVALID_FORMAT);
        }

        return input.split("\n");
    }
}

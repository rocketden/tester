package com.rocketden.tester.service.parsers;

import com.rocketden.tester.model.problem.Problem;
import com.rocketden.tester.model.problem.ProblemIOType;
import com.rocketden.tester.model.problem.ProblemInput;
import com.rocketden.tester.model.problem.ProblemTestCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class InputParserTests {

    @Spy
    @InjectMocks
    private InputParser inputParser;

    // Helper method to create problem and inputs from given parameters
    private Problem testCaseGenerator(String input, ProblemIOType... types) {
        ProblemTestCase testCase = new ProblemTestCase();
        testCase.setInput(input);

        List<ProblemInput> problemInputs = new ArrayList<>();
        for (ProblemIOType type : types) {
            ProblemInput problemInput = new ProblemInput();
            problemInput.setType(type);

            problemInputs.add(problemInput);
        }

        Problem problem = new Problem();
        problem.setProblemInputs(problemInputs);
        problem.setTestCases(Collections.singletonList(testCase));

        return problem;
    }

    @Test
    public void parseDouble() {
        ProblemIOType expected = ProblemIOType.DOUBLE;

        Problem problem = testCaseGenerator("56", expected);
        ProblemTestCase testCase = problem.getTestCases().get(0);

        List<Object> output = inputParser.parseTestCase(problem, testCase);

        assertEquals(1, output.size());
        assertEquals(56.0, output.get(0));
        assertEquals(expected.getClassType(), output.get(0).getClass());

    }

    @Test
    public void parseIntArray() {

    }

    @Test
    public void parseBooleanArray() {

    }

    @Test
    public void parseMultipleArguments() {

    }

    @Test
    public void parseFailsInvalidFormat() {

    }

    @Test
    public void parseFailsTypeMismatch() {
        // need to also test [1, "foo", true] mixed types
    }

    @Test
    public void parseFailsArrayTypeMismatch() {

    }

    @Test
    public void parseFailsInvalidNumInputs() {

    }
}

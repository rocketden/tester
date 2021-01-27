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

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class InputParserTests {

    @Spy
    @InjectMocks
    private InputParser inputParser;

    @Test
    public void parseDouble() {
        ProblemTestCase testCase = new ProblemTestCase();
        testCase.setInput("4");

        ProblemInput problemInput = new ProblemInput();
        problemInput.setType(ProblemIOType.INTEGER);

        Problem problem = new Problem();
        problem.setTestCases(Collections.singletonList(testCase));
        problem.setProblemInputs(Collections.singletonList(problemInput));

        List<Object> output = inputParser.parseTestCase(problem, testCase);

        assertEquals(1, output.size());
        assertEquals(ProblemIOType.INTEGER.getClassType(), output.get(0).getClass());

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

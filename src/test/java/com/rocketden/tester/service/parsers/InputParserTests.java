package com.rocketden.tester.service.parsers;

import com.rocketden.tester.exception.ParserError;
import com.rocketden.tester.exception.api.ApiException;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

        Problem problem = testCaseGenerator("56\n", expected);
        ProblemTestCase testCase = problem.getTestCases().get(0);

        List<Object> output = inputParser.parseTestCase(problem, testCase);

        assertEquals(1, output.size());
        assertEquals(56.0, output.get(0));
        assertEquals(expected.getClassType(), output.get(0).getClass());

    }

    @Test
    public void parseIntArray() {
        ProblemIOType expected = ProblemIOType.ARRAY_INTEGER;

        Problem problem = testCaseGenerator(" \n [1   ,4, 99,   ]", expected);
        ProblemTestCase testCase = problem.getTestCases().get(0);

        List<Object> output = inputParser.parseTestCase(problem, testCase);

        assertEquals(1, output.size());

        Integer[] array = (Integer[]) output.get(0);
        assertEquals(1, array[0]);
        assertEquals(4, array[1]);
        assertEquals(99, array[2]);
    }

    @Test
    public void parseBooleanArray() {
        ProblemIOType expected = ProblemIOType.ARRAY_BOOLEAN;

        Problem problem = testCaseGenerator("[true, \"true\", false, \"false\"]", expected);
        ProblemTestCase testCase = problem.getTestCases().get(0);

        List<Object> output = inputParser.parseTestCase(problem, testCase);

        assertEquals(1, output.size());

        Boolean[] array = (Boolean[]) output.get(0);
        assertTrue(array[0]);
        assertTrue(array[1]);
        assertFalse(array[2]);
        assertFalse(array[3]);
    }

    @Test
    public void parseMultipleArguments() {
        String input = "test\n[a, b, c]\n5";

        ProblemIOType type1 = ProblemIOType.STRING;
        ProblemIOType type2 = ProblemIOType.ARRAY_CHARACTER;
        ProblemIOType type3 = ProblemIOType.INTEGER;

        Problem problem = testCaseGenerator(input, type1, type2, type3);
        ProblemTestCase testCase = problem.getTestCases().get(0);

        List<Object> output = inputParser.parseTestCase(problem, testCase);

        assertEquals(3, output.size());

        assertEquals("test", output.get(0));
        assertEquals('a', ((Character[]) output.get(1))[0]);
        assertEquals(5, output.get(2));
    }

    @Test
    public void parseFailsInvalidFormat() {
        ProblemIOType expected = ProblemIOType.ARRAY_INTEGER;

        Problem problem = testCaseGenerator("{1, 2, 3}", expected);
        ProblemTestCase testCase = problem.getTestCases().get(0);

        ApiException exception = assertThrows(ApiException.class, () -> inputParser.parseTestCase(problem, testCase));

        assertEquals(ParserError.INVALID_INPUT, exception.getError());
    }

    @Test
    public void parseFailsTypeMismatch() {
        ProblemIOType expected = ProblemIOType.INTEGER;

        Problem problem = testCaseGenerator("5.5", expected);
        ProblemTestCase testCase = problem.getTestCases().get(0);

        ApiException exception = assertThrows(ApiException.class, () -> inputParser.parseTestCase(problem, testCase));

        assertEquals(ParserError.INVALID_INPUT, exception.getError());
    }

    @Test
    public void parseFailsNumberStringMismatch() {
        ProblemIOType expected = ProblemIOType.DOUBLE;

        Problem problem = testCaseGenerator("hello", expected);
        ProblemTestCase testCase = problem.getTestCases().get(0);

        ApiException exception = assertThrows(ApiException.class, () -> inputParser.parseTestCase(problem, testCase));

        assertEquals(ParserError.INVALID_INPUT, exception.getError());
    }

    @Test
    public void parseFailsArrayTypeMismatch() {
        ProblemIOType expected = ProblemIOType.ARRAY_CHARACTER;

        Problem problem = testCaseGenerator("[a, b, ccc]", expected);
        ProblemTestCase testCase = problem.getTestCases().get(0);

        ApiException exception = assertThrows(ApiException.class, () -> inputParser.parseTestCase(problem, testCase));

        assertEquals(ParserError.INVALID_INPUT, exception.getError());
    }

    @Test
    public void parseFailsInvalidNumInputs() {
        ProblemIOType type1 = ProblemIOType.STRING;
        ProblemIOType type2 = ProblemIOType.INTEGER;

        Problem problem = testCaseGenerator("hello", type1, type2);
        ProblemTestCase testCase = problem.getTestCases().get(0);

        ApiException exception = assertThrows(ApiException.class, () -> inputParser.parseTestCase(problem, testCase));

        assertEquals(ParserError.INCORRECT_COUNT, exception.getError());
    }
}

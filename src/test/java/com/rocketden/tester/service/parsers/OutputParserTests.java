package com.rocketden.tester.service.parsers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.rocketden.tester.exception.ParserError;
import com.rocketden.tester.exception.api.ApiException;
import com.rocketden.tester.model.problem.ProblemIOType;
import com.rocketden.tester.model.problem.ProblemTestCase;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OutputParserTests {

    @Spy
    @InjectMocks
    private OutputParser outputParser;

    /**
     * I/O: Fields to test different inputs and outputs.
     */

    private static final String STRING_OUTPUT_1 = "\"This is a test - hello!\"";
    private static final String STRING_OUTPUT_2 = "\"This is a test = hello!\"";

    // Lone strings require surrounding string quotes, but array strings do not.
    private static final String STRING_OUTPUT_INVALID = "This is a test - hello!";
    private static final ProblemIOType STRING_OUTPUT_TYPE = ProblemIOType.STRING;

    private static final String INTEGER_OUTPUT = "7";
    private static final ProblemIOType INTEGER_OUTPUT_TYPE = ProblemIOType.INTEGER;

    private static final String CHARACTER_OUTPUT_1 = "'A'";
    private static final String CHARACTER_OUTPUT_2 = "A";
    private static final ProblemIOType CHARACTER_OUTPUT_TYPE = ProblemIOType.CHARACTER;

    private static final String BOOLEAN_OUTPUT = "true";
    private static final ProblemIOType BOOLEAN_OUTPUT_TYPE = ProblemIOType.BOOLEAN;

    private static final String DOUBLE_OUTPUT_1 = "5.37";
    private static final String DOUBLE_OUTPUT_2 = "5.370000";
    private static final ProblemIOType DOUBLE_OUTPUT_TYPE = ProblemIOType.DOUBLE;

    private static final String ARRAY_STRING_OUTPUT_1 = "[\"adventure\", \"zoology\", \"impact\"]";
    private static final String ARRAY_STRING_OUTPUT_2 = "[adventure, zoology, impact]";
    private static final ProblemIOType ARRAY_STRING_OUTPUT_TYPE = ProblemIOType.ARRAY_STRING;

    private static final String ARRAY_INTEGER_OUTPUT = "[1, 2, 3]";
    private static final ProblemIOType ARRAY_INTEGER_OUTPUT_TYPE = ProblemIOType.ARRAY_INTEGER;

    private static final String ARRAY_CHARACTER_OUTPUT_1 = "['a', 'b', 'c']";
    private static final String ARRAY_CHARACTER_OUTPUT_2 = "[a, b, c]";
    private static final ProblemIOType ARRAY_CHARACTER_OUTPUT_TYPE = ProblemIOType.ARRAY_CHARACTER;

    private static final String ARRAY_BOOLEAN_OUTPUT = "[true, false, false]";
    private static final ProblemIOType ARRAY_BOOLEAN_OUTPUT_TYPE = ProblemIOType.ARRAY_BOOLEAN;

    private static final String ARRAY_DOUBLE_OUTPUT_1 = "[5, 6102.820, 0]";
    private static final String ARRAY_DOUBLE_OUTPUT_2 = "[5.0, 6102.82, 0]";
    private static final ProblemIOType ARRAY_DOUBLE_OUTPUT_TYPE = ProblemIOType.ARRAY_DOUBLE;

    @Test
    public void testIsOutputCorrectString() {
        ProblemTestCase testCase = new ProblemTestCase();
        testCase.setOutput(STRING_OUTPUT_1);
        assertTrue(outputParser.isOutputCorrect(STRING_OUTPUT_1, testCase, STRING_OUTPUT_TYPE));
    }

    @Test
    public void testIsOutputIncorrectString() {
        ProblemTestCase testCase = new ProblemTestCase();
        testCase.setOutput(STRING_OUTPUT_1);
        assertFalse(outputParser.isOutputCorrect(STRING_OUTPUT_2, testCase, STRING_OUTPUT_TYPE));
    }
    
    @Test
    public void testIsOutputCorrectInteger() {
        ProblemTestCase testCase = new ProblemTestCase();
        testCase.setOutput(INTEGER_OUTPUT);
        assertTrue(outputParser.isOutputCorrect(INTEGER_OUTPUT, testCase, INTEGER_OUTPUT_TYPE));
    }

    @Test
    public void testIsOutputCorrectCharacter() {
        ProblemTestCase testCase = new ProblemTestCase();
        testCase.setOutput(CHARACTER_OUTPUT_1);
        assertTrue(outputParser.isOutputCorrect(CHARACTER_OUTPUT_2, testCase, CHARACTER_OUTPUT_TYPE));
    }

    @Test
    public void testIsOutputCorrectBoolean() {
        ProblemTestCase testCase = new ProblemTestCase();
        testCase.setOutput(BOOLEAN_OUTPUT);
        assertTrue(outputParser.isOutputCorrect(BOOLEAN_OUTPUT, testCase, BOOLEAN_OUTPUT_TYPE));
    }

    @Test
    public void testIsOutputCorrectDouble() {
        ProblemTestCase testCase = new ProblemTestCase();
        testCase.setOutput(DOUBLE_OUTPUT_1);
        assertTrue(outputParser.isOutputCorrect(DOUBLE_OUTPUT_2, testCase, DOUBLE_OUTPUT_TYPE));
    }

    @Test
    public void testIsOutputCorrectArrayString() {
        ProblemTestCase testCase = new ProblemTestCase();
        testCase.setOutput(ARRAY_STRING_OUTPUT_1);
        assertTrue(outputParser.isOutputCorrect(ARRAY_STRING_OUTPUT_2, testCase, ARRAY_STRING_OUTPUT_TYPE));
    }

    @Test
    public void testIsOutputCorrectArrayInteger() {
        ProblemTestCase testCase = new ProblemTestCase();
        testCase.setOutput(ARRAY_INTEGER_OUTPUT);
        assertTrue(outputParser.isOutputCorrect(ARRAY_INTEGER_OUTPUT, testCase, ARRAY_INTEGER_OUTPUT_TYPE));
    }

    @Test
    public void testIsOutputCorrectArrayCharacter() {
        ProblemTestCase testCase = new ProblemTestCase();
        testCase.setOutput(ARRAY_CHARACTER_OUTPUT_1);
        assertTrue(outputParser.isOutputCorrect(ARRAY_CHARACTER_OUTPUT_2, testCase, ARRAY_CHARACTER_OUTPUT_TYPE));
    }

    @Test
    public void testIsOutputCorrectArrayBoolean() {
        ProblemTestCase testCase = new ProblemTestCase();
        testCase.setOutput(ARRAY_BOOLEAN_OUTPUT);
        assertTrue(outputParser.isOutputCorrect(ARRAY_BOOLEAN_OUTPUT, testCase, ARRAY_BOOLEAN_OUTPUT_TYPE));
    }

    @Test
    public void testIsOutputCorrectArrayDouble() {
        ProblemTestCase testCase = new ProblemTestCase();
        testCase.setOutput(ARRAY_DOUBLE_OUTPUT_1);
        assertTrue(outputParser.isOutputCorrect(ARRAY_DOUBLE_OUTPUT_2, testCase, ARRAY_DOUBLE_OUTPUT_TYPE));
    }

    @Test
    public void testIsOutputInvalidParseString() {
        ProblemTestCase testCase = new ProblemTestCase();
        testCase.setOutput(STRING_OUTPUT_1);
        ApiException exception = assertThrows(ApiException.class, () -> outputParser.isOutputCorrect(STRING_OUTPUT_INVALID, testCase, STRING_OUTPUT_TYPE));
        assertEquals(ParserError.INVALID_OUTPUT, exception.getError());
    }
}

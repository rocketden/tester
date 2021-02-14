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
    private static final String STRING_OUTPUT_INVALID = "This is a test - hello!";
    private static final ProblemIOType STRING_OUTPUT_TYPE = ProblemIOType.STRING;

    private static final String INTEGER_OUTPUT = "7";
    private static final ProblemIOType INTEGER_OUTPUT_TYPE = ProblemIOType.INTEGER;

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
    public void testIsOutputInvalidParseString() {
        ProblemTestCase testCase = new ProblemTestCase();
        testCase.setOutput(STRING_OUTPUT_1);
        ApiException exception = assertThrows(ApiException.class, () -> outputParser.isOutputCorrect(STRING_OUTPUT_INVALID, testCase, STRING_OUTPUT_TYPE));
        assertEquals(ParserError.INVALID_OUTPUT, exception.getError());
    }
}

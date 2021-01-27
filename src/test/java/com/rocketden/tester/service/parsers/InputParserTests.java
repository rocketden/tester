package com.rocketden.tester.service.parsers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class InputParserTests {

    @Spy
    @InjectMocks
    private InputParser inputParser;

    @Test
    public void parseDouble() {

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

    @Test
    public void splitTestCaseIntoInputsSuccess() {
        String input = "[1, 2, 3]\n4\ntrue";

        String[] output = inputParser.splitTestCaseIntoInputs(input);

        assertEquals(3, output.length);
    }
}

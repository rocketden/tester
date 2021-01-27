package com.rocketden.tester.service.parsers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

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
}

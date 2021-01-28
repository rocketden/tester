package com.rocketden.tester.service.generators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.rocketden.tester.exception.ProblemError;
import com.rocketden.tester.exception.api.ApiException;
import com.rocketden.tester.model.problem.ProblemIOType;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PythonDriverGeneratorServiceTests {

    @Spy
    @InjectMocks
    private PythonDriverGeneratorService service;

    // Default inputs for different data types.
    private final static String STRING_INPUT = "input";
    private final static Integer INTEGER_INPUT = 15;
    private final static Double DOUBLE_INPUT = 14.5;
    private final static Character CHARACTER_INPUT = 'Z';
    private final static Boolean BOOLEAN_INPUT = true;
    private final static String[] ARRAY_STRING_INPUT = {"i1", "i2", "i3"};
    private final static Integer[] ARRAY_INTEGER_INPUT = {-1, 2, 3};
    private final static Double[] ARRAY_DOUBLE_INPUT = {-5.5, 0.0, 3.111};
    private final static Character[] ARRAY_CHARACTER_INPUT = {'s', 'h', 'i'};
    private final static Boolean[] ARRAY_BOOLEAN_INPUT = {false, true, true};

    /**
     * INSTANTIATION TESTS
     */

    @Test
    public void typeInstantiationToString() {
        assertNull(service.typeInstantiationToString(null));
    }

    /**
     * INITIALIZATION TESTS
     */

    @Test
    public void typeInitializationToStringString() {
        assertEquals("\"input\"", service.typeInitializationToString(ProblemIOType.STRING, STRING_INPUT));
    }

    @Test
    public void typeInitializationToStringInteger() {
        assertEquals("15", service.typeInitializationToString(ProblemIOType.INTEGER, INTEGER_INPUT));
    }

    @Test
    public void typeInitializationToStringDouble() {
        assertEquals("14.500000", service.typeInitializationToString(ProblemIOType.DOUBLE, DOUBLE_INPUT));
    }

    @Test
    public void typeInitializationToStringCharacter() {
        assertEquals("'Z'", service.typeInitializationToString(ProblemIOType.CHARACTER, CHARACTER_INPUT));
    }

    @Test
    public void typeInitializationToStringBoolean() {
        assertEquals("true", service.typeInitializationToString(ProblemIOType.BOOLEAN, BOOLEAN_INPUT));
    }

    @Test
    public void typeInitializationToStringArrayString() {
        assertEquals("[\"i1\", \"i2\", \"i3\"]", 
            service.typeInitializationToString(ProblemIOType.ARRAY_STRING, ARRAY_STRING_INPUT));
    }

    @Test
    public void typeInitializationToStringArrayInteger() {
        assertEquals("[-1, 2, 3]", 
            service.typeInitializationToString(ProblemIOType.ARRAY_INTEGER, ARRAY_INTEGER_INPUT));
    }

    @Test
    public void typeInitializationToStringArrayDouble() {
        assertEquals("[-5.5, 0.0, 3.111]", 
            service.typeInitializationToString(ProblemIOType.ARRAY_DOUBLE, ARRAY_DOUBLE_INPUT));
    }

    @Test
    public void typeInitializationToStringArrayCharacter() {
        assertEquals("['s', 'h', 'i']", 
            service.typeInitializationToString(ProblemIOType.ARRAY_CHARACTER, ARRAY_CHARACTER_INPUT));
    }

    @Test
    public void typeInitializationToStringArrayBoolean() {
        assertEquals("[false, true, true]", 
            service.typeInitializationToString(ProblemIOType.ARRAY_BOOLEAN, ARRAY_BOOLEAN_INPUT));
    }

    @Test
    public void typeInitializationToStringNullError() {
        ApiException exception = assertThrows(ApiException.class, () ->
            service.typeInitializationToString(null, ARRAY_BOOLEAN_INPUT));
        assertEquals(ProblemError.OBJECT_MATCH_IOTYPE, exception.getError());
        ;
    }

    @Test
    public void typeInitializationToStringMismatchError() {
        ApiException exception = assertThrows(ApiException.class, () ->
            service.typeInitializationToString(ProblemIOType.STRING, CHARACTER_INPUT));
        assertEquals(ProblemError.OBJECT_MATCH_IOTYPE, exception.getError());
        ;
    }
}

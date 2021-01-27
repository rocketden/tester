package com.rocketden.tester.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.rocketden.tester.exception.ProblemError;
import com.rocketden.tester.exception.api.ApiException;
import com.rocketden.tester.model.problem.ProblemIOType;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProblemTests {
    @Test
    public void problemIOTypeSuccess() {
        assertEquals(ProblemIOType.STRING, ProblemIOType.valueOf("string"));
    }
    
    @Test
    public void problemIOTypeBadType() {
        ApiException exception = assertThrows(ApiException.class, () ->
            ProblemIOType.valueOf("nonexistenttype"));
        assertEquals(ProblemError.BAD_IOTYPE, exception.getError());
        ;
    }
}
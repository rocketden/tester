package com.rocketden.tester.service.generators;

import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    public void todo() {
        assertTrue(true);
    }
}
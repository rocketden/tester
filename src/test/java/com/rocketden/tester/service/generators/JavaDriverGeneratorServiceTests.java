package com.rocketden.tester.service.generators;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class JavaDriverGeneratorServiceTests {

    @Spy
    @InjectMocks
    private JavaDriverGeneratorService service;

    @Test
    public void todo() {
        assertTrue(true);
    }
}
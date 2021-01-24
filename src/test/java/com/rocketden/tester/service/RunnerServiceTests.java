package com.rocketden.tester.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RunnerServiceTests {

    @Spy
    @InjectMocks
    private RunnerService runnerService;

    @Test
    public void runSuccess() {

    }

    @Test
    public void runFails() {
        // 1. Returns the given ApiError
        // 2. deleteTempFolder is still called if an error occurs
    }
}

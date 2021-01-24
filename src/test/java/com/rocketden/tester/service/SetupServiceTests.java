package com.rocketden.tester.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SetupServiceTests {

    @Spy
    @InjectMocks
    private SetupService setupService;

    @Test
    public void createTempFolderSuccess() {
        // Driver and Solution files are supposedly created (mocked)
        // Returns correct folder path
    }

    @Test
    public void deleteTempFolderSuccess() {

    }

    @Test
    public void deleteTempFolderInvalidFolder() {
        
    }
}

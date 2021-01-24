package com.rocketden.tester.service;

import com.rocketden.tester.dto.RunRequest;
import com.rocketden.tester.model.Language;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RunnerServiceTests {

    @Mock
    private SetupService setupService;

    @Mock
    private DockerService dockerService;

    @Spy
    @InjectMocks
    private RunnerService runnerService;

    private static final String TEMP_FOLDER = "test";
    private static final Language LANGUAGE = Language.JAVA;

    @Test
    public void runSuccess() {
        RunRequest request = new RunRequest();
        request.setLanguage(LANGUAGE);

        runnerService.run(request);

        Mockito.doReturn(TEMP_FOLDER).when(setupService).createTempFolder(request);
        verify(dockerService).spawnAndRun(TEMP_FOLDER, request.getLanguage());

        verify(setupService).deleteTempFolder(TEMP_FOLDER);
    }

    @Test
    public void runFails() {
        // 1. Returns the given ApiError
        // 2. deleteTempFolder is still called if an error occurs
    }
}

package com.rocketden.tester.service;

import com.rocketden.tester.dto.RunDto;
import com.rocketden.tester.dto.RunRequest;
import com.rocketden.tester.exception.RequestError;
import com.rocketden.tester.exception.api.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RunnerService {

    private final SetupService setupService;
    private final DockerService dockerService;

    @Autowired
    public RunnerService(SetupService setupService, DockerService dockerService) {
        this.setupService = setupService;
        this.dockerService = dockerService;
    }

    public RunDto run(RunRequest request) {
        if (request.getCode() == null || request.getLanguage() == null || request.getProblem() == null) {
            throw new ApiException(RequestError.EMPTY_FIELD);
        }

        String folder = setupService.createTempFolder(request);
        try {
            return dockerService.spawnAndRun(folder, request.getLanguage());
        } finally {
            // Clean up temp folder regardless if above code throws an exception
            setupService.deleteTempFolder(folder);
        }
    }
}

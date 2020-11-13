package com.rocketden.tester.service;

import com.rocketden.tester.dto.RunDto;
import com.rocketden.tester.dto.RunRequest;
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
        String folder = setupService.createTempFolder(request);
        RunDto output = dockerService.spawnAndRun(folder);
        setupService.deleteTempFolder(folder);

        return output;
    }
}

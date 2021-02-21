package com.rocketden.tester.service;

import com.rocketden.tester.dto.RunDto;
import com.rocketden.tester.dto.RunRequest;
import com.rocketden.tester.exception.ProblemError;
import com.rocketden.tester.exception.RequestError;
import com.rocketden.tester.exception.api.ApiException;
import com.rocketden.tester.model.problem.Problem;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
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
        // Check base fields for null values
        if (request.getCode() == null || request.getLanguage() == null || request.getProblem() == null) {
            throw new ApiException(RequestError.EMPTY_FIELD);
        }

        // Check fields of problem class for null or empty fields
        Problem problem = request.getProblem();
        if (problem.getTestCases() == null || problem.getOutputType() == null
                || problem.getProblemInputs() == null || problem.getTestCases().isEmpty()) {
            throw new ApiException(RequestError.EMPTY_FIELD);
        }

        // Check fields of problem's parameter inputs for null values
        problem.getProblemInputs().forEach((input)-> {
            if (input.getType() == null || input.getName() == null || input.getName().isEmpty()) {
                throw new ApiException(ProblemError.BAD_PARAMETER_SETTINGS);
            }
        });

        log.info("CHECKING THAT UPDATE WORKS");
        String folder = setupService.createTempFolder();
        try {
            setupService.populateTempFolder(folder, request);
            return dockerService.spawnAndRun(folder, request.getLanguage(), problem);
        } finally {
            // Clean up temp folder regardless if above code throws an exception
            setupService.deleteTempFolder(folder);
            log.info("### END RUN REQUEST ###");
        }
    }
}

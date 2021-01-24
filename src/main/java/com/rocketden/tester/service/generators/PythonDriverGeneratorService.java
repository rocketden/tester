package com.rocketden.tester.service.generators;

import java.io.FileWriter;
import java.io.IOException;

import com.rocketden.tester.exception.DockerSetupError;
import com.rocketden.tester.exception.api.ApiException;
import com.rocketden.tester.model.problem.Problem;
import com.rocketden.tester.service.DriverGeneratorService;

import org.springframework.stereotype.Service;

@Service
public class PythonDriverGeneratorService implements DriverGeneratorService {
    @Override
    public void writeDriverFile(String fileDirectory, Problem problem) {
        // Open writer using try-with-resources.
        try (FileWriter writer = new FileWriter(fileDirectory)) {
            writer.write("print('This script is not running another script.');");
        } catch (IOException e) {
            throw new ApiException(DockerSetupError.WRITE_CODE_TO_DISK);
        }
    }

    @Override
    public void writeStartingBoilerplate() {
        // TODO Auto-generated method stub

    }

    @Override
    public void writeTestCases(Problem problem) {
        // TODO Auto-generated method stub

    }

    @Override
    public void writeExecuteTestCases(Problem problem) {
        // TODO Auto-generated method stub

    }

    @Override
    public void writeEndingBoilerplate() {
        // TODO Auto-generated method stub

    }
}

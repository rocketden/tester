package com.rocketden.tester.service;

import java.io.FileWriter;
import java.io.IOException;

import com.rocketden.tester.exception.DockerSetupError;
import com.rocketden.tester.exception.api.ApiException;
import com.rocketden.tester.model.Language;
import com.rocketden.tester.model.problem.Problem;

import org.springframework.stereotype.Service;

@Service
public class DriverFileService {

    public void writeDriverFile(String fileDirectory, Language language,
        Problem problem) {

        // Create a file with the contents of the code variable.
        FileWriter writer = null;
        try {
            writer = new FileWriter(fileDirectory);
            writer.write("This is a test.");
            writer.write("This is a test now.");
        } catch (IOException e) {
            throw new ApiException(DockerSetupError.WRITE_CODE_TO_DISK);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    throw new ApiException(DockerSetupError.WRITE_CODE_TO_DISK);
                }
            }
        }
    }
}

package com.rocketden.tester.service;

import com.rocketden.tester.dto.RunRequest;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class SetupService {

    public String createTempFolder(RunRequest request) {
        String folder = generateRandomFolder();
        boolean success = new File(folder).mkdirs();

        if (success) {
            String code = request.getCode();
            String driverFile = String.format("%s/run.sh", folder);

            try {
                // Create a file called run.sh with the contents of the code variable
                Files.write(Paths.get(driverFile), code.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                // Error handling
                System.out.println("An error occurred");
            }
        }

        return folder;
    }

    private String generateRandomFolder() {
        return "folder12";
    }
}

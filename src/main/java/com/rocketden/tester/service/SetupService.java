package com.rocketden.tester.service;

import com.rocketden.tester.dto.RunRequest;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class SetupService {

    public String createTempFolder(RunRequest request) {
        String pwd = Paths.get("").toAbsolutePath().toString();
        String relativePath = "src/main/java/com/rocketden/tester";
        String folder = String.format("%s/%s/temp/%s", pwd, relativePath, generateRandomFolder());
        boolean success = new File(folder).mkdirs();

        if (success) {
            String code = request.getCode();
            System.out.println(code);
            String driverFile = String.format("%s/run.sh", folder);

            try {
                // Create a file called run.sh with the contents of the code variable
                Files.write(Paths.get(driverFile), code.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                // Error handling
                System.out.println("An error occurred");
            }
        } else {
            // Error handling
            System.out.println("An error occurred");
        }

        return folder;
    }

    public void deleteTempFolder(String folder) {
        try {
            FileUtils.deleteDirectory(new File(folder));
        } catch (IOException e) {
            // Error handling
            System.out.println("An error occurred");
        }
    }

    private String generateRandomFolder() {
        return "folder12345";
    }
}

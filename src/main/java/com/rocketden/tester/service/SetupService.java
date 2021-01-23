package com.rocketden.tester.service;

import com.rocketden.tester.dto.RunRequest;
import com.rocketden.tester.exception.DockerSetupError;
import com.rocketden.tester.exception.api.ApiException;
import com.rocketden.tester.model.Language;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class SetupService {

    public String createTempFolder(RunRequest request) {
        Language language = request.getLanguage();
        
        String pwd = Paths.get("").toAbsolutePath().toString();
        String relativePath = "src/main/java/com/rocketden/tester";
        String folder = String.format("%s/%s/temp/%s", pwd, relativePath, generateRandomFolderName());
        boolean success = new File(folder).mkdirs();

        if (success) {
            String code = request.getCode();
            String solutionFile = String.format("%s/Solution.%s/", folder, language.getExtension());

            try {
                // Create a file with the contents of the code variable
                Files.write(Paths.get(solutionFile), code.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                throw new ApiException(DockerSetupError.WRITE_USER_CODE);
            }
        } else {
            throw new ApiException(DockerSetupError.CREATE_TEMP_FOLDER);
        }

        return folder;
    }

    public void deleteTempFolder(String folder) {
        try {
            FileUtils.deleteDirectory(new File(folder));
        } catch (IOException e) {
            throw new ApiException(DockerSetupError.DELETE_TEMP_FOLDER);
        }
    }

    private String generateRandomFolderName() {
        return RandomStringUtils.randomAlphanumeric(10);
    }
}

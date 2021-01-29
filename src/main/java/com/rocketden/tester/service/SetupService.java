package com.rocketden.tester.service;

import com.rocketden.tester.dto.RunRequest;
import com.rocketden.tester.exception.DockerSetupError;
import com.rocketden.tester.exception.api.ApiException;
import com.rocketden.tester.model.Language;
import com.rocketden.tester.model.problem.Problem;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class SetupService {

    private final DriverFileService driverFileService;

    @Autowired
    public SetupService(DriverFileService driverFileService) {
        this.driverFileService = driverFileService;
    }

    public String createTempFolder() {
        String pwd = Paths.get("").toAbsolutePath().toString();
        String relativePath = "src/main/java/com/rocketden/tester";
        String folder = String.format("%s/%s/temp/%s", pwd, relativePath, generateRandomFolderName());

        boolean success = new File(folder).mkdirs();
        if (success) {
            return folder;
        }
        throw new ApiException(DockerSetupError.CREATE_TEMP_FOLDER);
    }

    public void populateTempFolder(String folder, RunRequest request) {
        Language language = request.getLanguage();
        Problem problem = request.getProblem();

        String code = request.getCode();
        String driverFile = String.format("%s/Driver.%s", folder, language.getExtension());
        String solutionFile = String.format("%s/Solution.%s", folder, language.getExtension());

        try {
            // Create a file with the contents of the code variable
            driverFileService.writeDriverFile(driverFile, language, problem);
            Files.write(Paths.get(solutionFile), code.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new ApiException(DockerSetupError.WRITE_CODE_TO_DISK);
        }
    }

    public void deleteTempFolder(String folder) {
        if (folder.contains("..") || !folder.contains("src/main/java/com/rocketden/tester/temp/")) {
            throw new ApiException(DockerSetupError.INVALID_DELETE_PATH);
        }

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

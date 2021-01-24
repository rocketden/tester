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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SetupService {

    private final List<DriverGeneratorService> driverGeneratorServiceList;
    private final Map<String, DriverGeneratorService> driverGeneratorServiceMap;

    @Autowired
    public SetupService(List<DriverGeneratorService>
        driverGeneratorServiceList) {
        this.driverGeneratorServiceList = driverGeneratorServiceList;

        // Create the map from the DriverGeneratorService list.
        this.driverGeneratorServiceMap = new HashMap<>();
        this.driverGeneratorServiceList.forEach(driverGeneratorService ->
            this.driverGeneratorServiceMap.put(driverGeneratorService.getClass().getSimpleName(), driverGeneratorService)
        );
    }

    public String createTempFolder(RunRequest request) {
        Language language = request.getLanguage();
        Problem problem = request.getProblem();
        
        String pwd = Paths.get("").toAbsolutePath().toString();
        String relativePath = "src/main/java/com/rocketden/tester";
        String folder = String.format("%s/%s/temp/%s", pwd, relativePath, generateRandomFolderName());
        boolean success = new File(folder).mkdirs();

        if (success) {
            String code = request.getCode();
            String driverFile = String.format("%s/Driver.%s/", folder, language.getExtension());
            String solutionFile = String.format("%s/Solution.%s/", folder, language.getExtension());

            try {
                DriverGeneratorService driverGeneratorService =
                    driverGeneratorServiceMap.get(language.getDriverGeneratorName());
                // Create a file with the contents of the code variable
                driverGeneratorService.writeDriverFile(driverFile, problem);
                Files.write(Paths.get(solutionFile), code.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                throw new ApiException(DockerSetupError.WRITE_CODE_TO_DISK);
            }
        } else {
            throw new ApiException(DockerSetupError.CREATE_TEMP_FOLDER);
        }

        return folder;
    }

    public void deleteTempFolder(String folder) {
        // TODO: add check to only allow folders inside temp to be deleted
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

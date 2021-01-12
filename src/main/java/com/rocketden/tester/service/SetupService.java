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
        String extension = "";
        String language = request.getLanguage();
        String path = "";
        switch (language){
            case "docker_python":
                extension = "py";
                path = "python";
                break;
            case "docker_ruby":
                extension = "rb";
                path = "ruby";
                break; 
            case "docker_swift":
                extension = "swift";
                path = "swift";
                break; 
            case "docker_cpp":
                extension = "cpp";
                path = "cpp";
                break;
            case "docker_php":
                extension = "php";
                path = "php";
                break;
            case "docker_c":
                extension = "c";
                path = "gcc";
                break;
            case "docker_java":
                extension = "java";
                path = "java";
                break;
            case "docker_rust":
                extension = "rc";
                path = "rust";
                break;
            case "docker_bash":
                path = "bash";
                extension = "sh";
                break;
            }
        
        String pwd = Paths.get("").toAbsolutePath().toString();
        String relativePath = String.format("src/main/docker/%s", path); 
        String folder = String.format("%s/%s/%s", pwd, relativePath, generateRandomFolder());
        boolean success = new File(folder).mkdirs();

        if (success) {
            String code = request.getCode();
            String driverFile = String.format("%s/script.%s/", folder, extension);

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
        return "code";
    }
}

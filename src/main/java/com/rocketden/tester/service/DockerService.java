package com.rocketden.tester.service;

import com.rocketden.tester.dto.RunDto;
import com.rocketden.tester.exception.DockerSetupError;
import com.rocketden.tester.exception.api.ApiException;
import com.rocketden.tester.model.Language;
import com.rocketden.tester.model.problem.Problem;
import com.rocketden.tester.service.parsers.OutputParser;

import org.springframework.stereotype.Service;

@Service
public class DockerService {

    public RunDto spawnAndRun(String folder, Language language, Problem problem) {
        try {
            // Create and run disposable docker container with the given temp folder
            String[] commands = getRunCommands(folder, language);
            ProcessBuilder builder = new ProcessBuilder(commands);
            builder.redirectErrorStream(true);
            Process process = builder.start();

            OutputParser outputParser = new OutputParser();
            return outputParser.parseCaptureOutput(process, problem);

        } catch (Exception e) {
            throw new ApiException(DockerSetupError.BUILD_DOCKER_CONTAINER);
        }
    }

    private String[] getRunCommands(String folder, Language language) {
        String mountPath = String.format("%s:/code", folder);
        return new String[] {"docker", "run", "--rm", "-v", mountPath, "-t", language.getDockerContainer()};
    }
}

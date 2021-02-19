package com.rocketden.tester.service;

import com.rocketden.tester.dto.RunDto;
import com.rocketden.tester.exception.DockerSetupError;
import com.rocketden.tester.exception.api.ApiException;
import com.rocketden.tester.model.Language;
import com.rocketden.tester.model.problem.Problem;
import com.rocketden.tester.service.parsers.OutputParser;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class DockerService {

    private final OutputParser outputParser;

    @Autowired
    public DockerService(OutputParser outputParser) {
        this.outputParser = outputParser;
    }

    public RunDto spawnAndRun(String folder, Language language, Problem problem) {
        try {
            // Create and run disposable docker container with the given temp folder
            log.info("Creating and running Docker container");
            String[] commands = getRunCommands(folder, language);
            ProcessBuilder builder = new ProcessBuilder(commands);
            builder.redirectErrorStream(true);
            Process process = builder.start();

            log.info("Docker creation process successfully initiated");

            // Parse, capture, and return the newly-created RunDto object.
            return outputParser.parseCaptureOutput(process, problem);
        } catch (ApiException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ApiException(DockerSetupError.BUILD_DOCKER_CONTAINER);
        }
    }

    private String[] getRunCommands(String folder, Language language) {
        String mountPath = String.format("%s:/code", folder);
        return new String[] {"docker", "run", "--rm", "-v", mountPath, "-t", language.getDockerContainer()};
    }
}

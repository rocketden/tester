package com.rocketden.tester.service;

import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class DockerService {

    public String spawnAndRun(String folder) {
        try {
            // First ensure that docker image is built: docker build --tag 'rocketden/tester' ../docker
            // TODO: figure out how to do this

            // Mount folder to docker container
            String mount = String.format("$PWD/../temp/%s:/app", folder);

            // Run docker container with copied folder, and delete container afterwards
            String[] runCommands = {"docker", "run", "-v", mount, "-t", "rocketden/tester", "--rm"};
            Runtime.getRuntime().exec(runCommands);

            // TODO: check for output file with results

        } catch (IOException e) {
            // Error handling
            System.out.println("An error occurred");
        }

        return "output";
    }
}

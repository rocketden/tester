package com.rocketden.tester.service;

import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class DockerService {

    public String spawnAndRun(String folder) {
        try {
            // First ensure that docker image is built: docker build --tag 'tester' ../docker
            // TODO: figure out how to do this

            // Create and run a new container with the given folder and scripts
            // TODO: figure out how to do this
            String[] runCommands = {"docker", "run", "tester"};
            Runtime.getRuntime().exec(runCommands);

        } catch (IOException e) {
            // Error handling
            System.out.println("An error occurred");
        }

        return "output";
    }
}

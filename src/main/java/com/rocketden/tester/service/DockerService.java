package com.rocketden.tester.service;

import org.springframework.stereotype.Service;

@Service
public class DockerService {

    public String spawnAndRun(String folder) {
        // This creates a disposable docker container with the contents from the given folder
        // And then runs and returns the output
        return "output";
    }
}

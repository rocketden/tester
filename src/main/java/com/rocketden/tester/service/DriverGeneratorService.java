package com.rocketden.tester.service;

import com.rocketden.tester.model.problem.Problem;

import org.springframework.stereotype.Service;

@Service
public interface DriverGeneratorService {

    void writeDriverFile(String fileDirectory, Problem problem);

    void writeStartingBoilerplate();

    void writeTestCases(Problem problem);

    void writeExecuteTestCases(Problem problem);

    void writeEndingBoilerplate();
    
}

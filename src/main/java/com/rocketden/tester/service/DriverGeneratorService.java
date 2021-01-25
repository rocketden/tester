package com.rocketden.tester.service;

import com.rocketden.tester.model.problem.Problem;
import com.rocketden.tester.model.problem.ProblemIOType;

import org.springframework.stereotype.Service;

@Service
public interface DriverGeneratorService {

    void writeDriverFile(String fileDirectory, Problem problem);

    void writeStartingBoilerplate();

    void writeTestCases(Problem problem);

    void writeExecuteTestCases(Problem problem);

    void writeEndingBoilerplate();

    void writeToStringCode();

    // The implementation of the type's instantiation.
    String typeInstantiationToString(ProblemIOType ioType);

    // The implementation of the object's initialization.
    String typeInitializationToString(ProblemIOType ioType, Object[] values);

}

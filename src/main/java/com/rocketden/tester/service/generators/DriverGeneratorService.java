package com.rocketden.tester.service.generators;

import java.io.FileWriter;
import java.io.IOException;

import com.rocketden.tester.model.problem.Problem;
import com.rocketden.tester.model.problem.ProblemIOType;

import org.springframework.stereotype.Service;

@Service
public interface DriverGeneratorService {

    void writeDriverFile(String fileDirectory, Problem problem);

    void writeStartingBoilerplate(FileWriter writer, Problem problem) throws IOException;

    void writeTestCases(FileWriter writer, Problem problem) throws IOException;

    void writeExecuteTestCases(FileWriter writer, Problem problem) throws IOException;

    void writeEndingBoilerplate(FileWriter writer) throws IOException;

    String getToStringCode(ProblemIOType outputType) throws IOException;

    // The implementation of the type's instantiation.
    String typeInstantiationToString(ProblemIOType ioType);

    // The implementation of the object's initialization.
    String typeInitializationToString(ProblemIOType ioType, Object value);

}

package com.rocketden.tester.service.generators;

import java.io.FileWriter;
import java.io.IOException;

import com.rocketden.tester.exception.DockerSetupError;
import com.rocketden.tester.exception.api.ApiException;
import com.rocketden.tester.model.problem.Problem;
import com.rocketden.tester.service.DriverGeneratorService;

import org.springframework.stereotype.Service;

@Service
public class PythonDriverGeneratorService implements DriverGeneratorService {
    @Override
    public void writeDriverFile(String fileDirectory, Problem problem) {
        // Open writer using try-with-resources.
        try (FileWriter writer = new FileWriter(fileDirectory)) {
            // Boilerplate starting setup.
            writer.write("import traceback\n");
            writer.write("import os\n");
            writer.write("os.chdir(os.getcwd())\n\n");

            // Import requires method name.
            writer.write("from Solution import multiplyDouble\n\n");
            writer.write("def main():\n\n");

            // Write test cases.
            writer.write("\ttest1var1 = 2\n");
            writer.write("\ttest2var1 = 5\n");
            writer.write("\ttest3var1 = 13\n");

            // Write test cases.
            for (int testCase = 1; testCase <= 3; testCase++) {
                writer.write(String.format("\tprint('Console (%d):')%n", testCase));
                // Use try-catch block to print any errors.
                writer.write("\ttry:\n");
                writer.write(String.format("\t\tsolution%d = multiplyDouble(test%dvar1)%n", testCase, testCase));
                writer.write(String.format("\t\tprint('Solution (%d):')%n", testCase));
                writer.write(String.format("\t\tprint(solution%d)%n", testCase));
                writer.write("\texcept Exception as e:\n");
                writer.write(String.format("\t\tprint('Error (%d):')%n", testCase));
                writer.write("\t\ttraceback.print_exc()\n");
            }
            
            // Boilerplate ending setup.
            writer.write("if __name__ == \"__main__\":\n");
            writer.write("\tmain()\n");
        } catch (IOException e) {
            throw new ApiException(DockerSetupError.WRITE_CODE_TO_DISK);
        }
    }



    @Override
    public void writeStartingBoilerplate() {
        // TODO Auto-generated method stub

    }

    @Override
    public void writeTestCases(Problem problem) {
        // TODO Auto-generated method stub

    }

    @Override
    public void writeExecuteTestCases(Problem problem) {
        // TODO Auto-generated method stub

    }

    @Override
    public void writeEndingBoilerplate() {
        // TODO Auto-generated method stub

    }
}

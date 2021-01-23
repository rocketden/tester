package com.rocketden.tester.service;

import java.io.FileWriter;
import java.io.IOException;

import com.rocketden.tester.exception.DockerSetupError;
import com.rocketden.tester.exception.api.ApiException;
import com.rocketden.tester.model.Language;
import com.rocketden.tester.model.problem.Problem;

import org.springframework.stereotype.Service;

@Service
public class DriverFileService {

    public void writeDriverFile(String fileDirectory, Language language,
        Problem problem) {

        // Open writer using try-with-resources.
        try (FileWriter writer = new FileWriter(fileDirectory)) {
            // Boilerplate starting setup.
            writer.write("public class Driver {\n");
            writer.write("\tpublic static void main (String[] args) {\n");

            // Write test cases.
            writer.write("\t\tInteger test1var1 = 2;\n");
            writer.write("\t\tInteger test2var1 = 5;\n");
            writer.write("\t\tInteger test3var1 = 13;\n");

            // Get solutions from the user code.
            writer.write("\t\tSolution solution = new Solution();\n");

            // Write test cases.
            for (int testCase = 1; testCase <= 3; testCase++) {
                writer.write(String.format("\t\tSystem.out.println(\"Console (%d):\");%n", testCase));
                // Use try-catch block to print any errors.
                writer.write("\t\ttry {\n");
                writer.write(String.format("\t\t\tInteger solution%d = solution.multiplyDouble(test%dvar1);%n", testCase, testCase));
                writer.write(String.format("\t\t\tSystem.out.println(\"Solution (%d):\");%n", testCase));
                writer.write(String.format("\t\t\tSystem.out.println(solution%d.toString());%n", testCase));
                writer.write("\t\t} catch (Exception e) {\n");
                writer.write(String.format("\t\t\tSystem.out.println(\"Error (%d):\");%n", testCase));
                writer.write("\t\t\te.printStackTrace();\n");
                writer.write("\t\t}\n");
            }
            
            // Boilerplate ending setup.
            writer.write("\t}\n");
            writer.write("}\n");
        } catch (IOException e) {
            throw new ApiException(DockerSetupError.WRITE_CODE_TO_DISK);
        }
    }
}

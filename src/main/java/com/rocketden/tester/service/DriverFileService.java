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

            // First test case.
            writer.write("\t\tSystem.out.println(\"Console (1):\");\n");
            writer.write("\t\ttry {\n");
            writer.write("\t\t\tInteger solution1 = solution.multiplyDouble(test1var1);\n");
            writer.write("\t\t\tSystem.out.println(\"Solution (1):\");\n");
            writer.write("\t\t\tSystem.out.println(solution1.toString());\n");
            writer.write("\t\t} catch (Exception e) {\n");
            writer.write("\t\t\tSystem.out.println(\"Error (1):\");\n");
            writer.write("\t\t\tSystem.out.println(e.getMessage());\n");
            writer.write("\t\t}\n");

            // Second test case.
            writer.write("\t\tSystem.out.println(\"Console (2):\");\n");
            writer.write("\t\ttry {\n");
            writer.write("\t\t\tInteger solution2 = solution.multiplyDouble(test2var1);\n");
            writer.write("\t\t\tSystem.out.println(\"Solution (2):\");\n");
            writer.write("\t\t\tSystem.out.println(solution2.toString());\n");
            writer.write("\t\t} catch (Exception e) {\n");
            writer.write("\t\t\tSystem.out.println(\"Error (2):\");\n");
            writer.write("\t\t\tSystem.out.println(e.getMessage());\n");
            writer.write("\t\t}\n");

            // Third test case.
            writer.write("\t\tSystem.out.println(\"Console (3):\");\n");
            writer.write("\t\ttry {\n");
            writer.write("\t\t\tInteger solution3 = solution.multiplyDouble(test3var1);\n");
            writer.write("\t\t\tSystem.out.println(\"Solution (3):\");\n");
            writer.write("\t\t\tSystem.out.println(solution3.toString());\n");
            writer.write("\t\t} catch (Exception e) {\n");
            writer.write("\t\t\tSystem.out.println(\"Error (3):\");\n");
            writer.write("\t\t\tSystem.out.println(e.getMessage());\n");
            writer.write("\t\t}\n");

            // Boilerplate ending setup.
            writer.write("\t}\n");
            writer.write("}\n");
        } catch (IOException e) {
            throw new ApiException(DockerSetupError.WRITE_CODE_TO_DISK);
        }
    }
}

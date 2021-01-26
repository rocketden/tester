package com.rocketden.tester.service.generators;

import java.io.FileWriter;
import java.io.IOException;

import com.rocketden.tester.exception.DockerSetupError;
import com.rocketden.tester.exception.ProblemError;
import com.rocketden.tester.exception.api.ApiException;
import com.rocketden.tester.model.problem.Problem;
import com.rocketden.tester.model.problem.ProblemIOType;
import com.rocketden.tester.service.DriverGeneratorService;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class JavaDriverGeneratorService implements DriverGeneratorService {
    @Override
    public void writeDriverFile(String fileDirectory, Problem problem) {
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
                writer.write(String.format("\t\t\tInteger solution%d = solution.multiplyDouble(test%dvar1);%n",
                        testCase, testCase));
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

    @Override
    public void writeToStringCode() {
        // TODO Auto-generated method stub

    }

    @Override
    public String typeInstantiationToString(ProblemIOType ioType) {
        switch (ioType) {
            case STRING:
                return "String";
            case INTEGER:
                return "int";
            case DOUBLE:
                return "double";
            case CHARACTER:
                return "char";
            case BOOLEAN:
                return "boolean";
            case ARRAY_STRING:
                return "String[]";
            case ARRAY_INTEGER:
                return "int[]";
            case ARRAY_DOUBLE:
                return "double[]";
            case ARRAY_CHARACTER:
                return "char[]";
            case ARRAY_BOOLEAN:
                return "boolean[]";
            default:
                return "";
        }
    }

    @Override
    public String typeInitializationToString(ProblemIOType ioType, Object value) {
        if (!ioType.typeMatches(value)) {
            throw new ApiException(ProblemError.OBJECT_MATCH_IOTYPE);
        }

        switch (ioType) {
            case STRING:
                return String.format("\"%s\"", (String) value);
            case INTEGER:
                return String.format("%d", (Integer) value);
            case DOUBLE:
                return String.format("%f", (Double) value);
            case CHARACTER:
                return String.format("'%c'", (Character) value);
            case BOOLEAN:
                return String.format("%b", (Boolean) value);
            case ARRAY_STRING:
                return String.format("{\"%s\"}", String.join("\", \"", (String[]) value));
            case ARRAY_INTEGER:
                return String.format("{%s}", StringUtils.join((Integer[]) value, ", "));
            case ARRAY_DOUBLE:
                return String.format("{%s}", StringUtils.join((Double[]) value, ", "));
            case ARRAY_CHARACTER:
                return String.format("{'%s'}", StringUtils.join((Character[]) value, "', '"));
            case ARRAY_BOOLEAN:
                return String.format("{%s}", StringUtils.join((Boolean[]) value, ", "));
            default:
                return "";
        }
    }
}
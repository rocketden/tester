package com.rocketden.tester.service.generators;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.rocketden.tester.exception.DockerSetupError;
import com.rocketden.tester.exception.ProblemError;
import com.rocketden.tester.exception.api.ApiException;
import com.rocketden.tester.model.problem.Problem;
import com.rocketden.tester.model.problem.ProblemIOType;
import com.rocketden.tester.model.problem.ProblemTestCase;
import com.rocketden.tester.service.DriverGeneratorService;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class JavaDriverGeneratorService implements DriverGeneratorService {
    @Override
    public void writeDriverFile(String fileDirectory, Problem problem) {
        // Open writer using try-with-resources.
        try (FileWriter writer = new FileWriter(fileDirectory)) {
            writeStartingBoilerplate(writer);
            writeTestCases(writer, problem);
            writeExecuteTestCases(writer, problem);
            writeEndingBoilerplate(writer);
        } catch (IOException e) {
            throw new ApiException(DockerSetupError.WRITE_CODE_TO_DISK);
        }
    }

    @Override
    public void writeStartingBoilerplate(FileWriter writer) throws IOException {
        writer.write("public class Driver {\n");
        writer.write("\tpublic static void main (String[] args) {\n");
    }

    @Override
    public void writeTestCases(FileWriter writer, Problem problem) throws IOException {

        /**
         * Write the test case input parameter variables at the top of the file.
         * Iterate through the test cases, and then each of the problem inputs;
         * these are used to construct the input parameter variables.
         */
        int testNum = 1;
        for (ProblemTestCase testCase : problem.getTestCases()) {

            // Iterate through the entries of inputs, which hold name and type.
            for (Entry<String, ProblemIOType> input : problem.getInputNameTypeMap().entrySet()) {

                // Get instantiation to hold the input type.
                String instantiation = typeInstantiationToString(input.getValue());

                // Get the input parameter variable name.
                String inputName = input.getKey();

                // Get initialization of input from its type and content.
                Gson gson = new Gson();
                String initialization = 
                    typeInitializationToString(
                        input.getValue(),
                        gson.fromJson(
                            testCase.getInput(),
                            input.getValue().getClassType()
                        )
                    );

                /**
                 * Write the creation of the test input variable.
                 * Format: [type] [input name][test num] = [input content];
                 * Ex: int num1 = 5;
                 */       
                writer.write(
                    String.format("\t\t%s %s%d = %s;%n",
                        instantiation,
                        inputName,
                        testNum,
                        initialization
                    )
                );
            }

            // Update the test number, used to distinguish the input params.
            testNum++;
        }
    }

    @Override
    public void writeExecuteTestCases(FileWriter writer, Problem problem) throws IOException {
        // Get solutions from the user code.
        writer.write("\t\tSolution solution = new Solution();\n");

        // Write test cases.
        int testNum = 1;
        String output = typeInstantiationToString(problem.getOutputType());
        for (ProblemTestCase tc : problem.getTestCases()) {
            writer.write(String.format("\t\tSystem.out.println(\"Console (%d):\");%n", testNum));
            // Use try-catch block to print any errors.
            writer.write("\t\ttry {\n");
            writer.write(String.format("\t\t\t%s solution%d = solution.multiplyDouble(", output, testNum));
            
            // Record multiple inputs.
            int inputNum = 1;
            for (Entry<String, ProblemIOType> input : problem.getInputNameTypeMap().entrySet()) {
                writer.write(String.format("%s%d", input.getKey(),
                    testNum));

                // Add comma + space, if more inputs are present.
                if (inputNum != problem.getInputNameTypeMap().size()) {
                    writer.write(", ");
                }
                
                inputNum++;
            }
            writer.write(");\n");

            writer.write(String.format("\t\t\tSystem.out.println(\"Solution (%d):\");%n", testNum));
            writer.write(String.format("\t\t\tSystem.out.println(solution%d);%n", testNum));
            writer.write("\t\t} catch (Exception e) {\n");
            writer.write(String.format("\t\t\tSystem.out.println(\"Error (%d):\");%n", testNum));
            writer.write("\t\t\te.printStackTrace();\n");
            writer.write("\t\t}\n");

            testNum++;
        }
    }

    @Override
    public void writeEndingBoilerplate(FileWriter writer) throws IOException {
        writer.write("\t}\n");
        writer.write("}\n");
    }

    @Override
    public void writeToStringCode() {
        // TODO Auto-generated method stub

    }

    @Override
    public String typeInstantiationToString(ProblemIOType ioType) {
        if (ioType == null) {
            return null;
        }

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
        if (ioType == null || value == null || !ioType.typeMatches(value)) {
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

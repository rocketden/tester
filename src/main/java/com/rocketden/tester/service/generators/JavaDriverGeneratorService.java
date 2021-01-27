package com.rocketden.tester.service.generators;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import com.google.gson.Gson;
import com.rocketden.tester.exception.DockerSetupError;
import com.rocketden.tester.exception.ProblemError;
import com.rocketden.tester.exception.api.ApiException;
import com.rocketden.tester.model.Language;
import com.rocketden.tester.model.problem.Problem;
import com.rocketden.tester.model.problem.ProblemIOType;
import com.rocketden.tester.model.problem.ProblemInput;
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
            for (ProblemInput input : problem.getProblemInputs()) {

                // Get instantiation to hold the input type.
                String instantiation = typeInstantiationToString(input.getType());

                // Get the input parameter variable name.
                String inputName = input.getName();

                // Get initialization of input from its type and content.
                Gson gson = new Gson();
                String initialization = 
                    typeInitializationToString(
                        input.getType(),
                        gson.fromJson(
                            testCase.getInput(),
                            input.getType().getClassType()
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
        // Get output to hold the instantiation output (return) type.
        String outputType = typeInstantiationToString(problem.getOutputType());

        // Execute each of the test cases within separate try-catch blocks.
        for (int testNum = 1; testNum <= problem.getTestCases().size(); testNum++) {
            // Print line to predict any console output.
            writer.write(String.format("\t\tSystem.out.println(\"Console (%d):\");%n", testNum));

            // Use try-catch block to print any errors.
            writer.write("\t\ttry {\n");

            // Write the base setup (w/o parameters) of calling user's solution.
            writer.write(String.format("\t\t\t%s solution%d = new Solution().%s(", outputType, testNum, problem.getMethodNames().get(Language.JAVA)));
            
            // Record the input (parameter) names for the function call.
            Iterator<ProblemInput> inputIterator = problem.getProblemInputs().iterator();
            while (inputIterator.hasNext()) {
                ProblemInput input = inputIterator.next();

                // Write the input (parameter) variable name.
                writer.write(String.format("%s%d", input.getName(), testNum));

                // Add comma + space, if more inputs are present.
                if (inputIterator.hasNext()) {
                    writer.write(", ");
                }
            }

            // End the call of the user's function / code.
            writer.write(");\n");

            // Print line to predict, then print, any solution output.
            writer.write(String.format("\t\t\tSystem.out.println(\"Solution (%d):\");%n", testNum));
            writer.write(String.format("\t\t\tSystem.out.println(solution%d);%n", testNum));

            // Catch and print any errors that arise from calling user's code.
            writer.write("\t\t} catch (Exception e) {\n");
            writer.write(String.format("\t\t\tSystem.out.println(\"Error (%d):\");%n", testNum));
            writer.write("\t\t\te.printStackTrace();\n");
            writer.write("\t\t}\n");
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
            throw new ApiException(ProblemError.BAD_IOTYPE);
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
                throw new ApiException(ProblemError.BAD_IOTYPE);
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

package com.rocketden.tester.service.generators;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import com.rocketden.tester.exception.DockerSetupError;
import com.rocketden.tester.exception.ProblemError;
import com.rocketden.tester.exception.api.ApiException;
import com.rocketden.tester.model.problem.Problem;
import com.rocketden.tester.model.problem.ProblemIOType;
import com.rocketden.tester.model.problem.ProblemInput;
import com.rocketden.tester.model.problem.ProblemTestCase;

import com.rocketden.tester.service.parsers.InputParser;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JavaDriverGeneratorService implements DriverGeneratorService {

    private final InputParser inputParser;

    @Autowired
    public JavaDriverGeneratorService(InputParser inputParser) {
        this.inputParser = inputParser;
    }

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
        writer.write(String.join("\n",
            "public class Driver {",
            "\tpublic static void main (String[] args) {\n"
        ));
    }

    @Override
    public void writeTestCases(FileWriter writer, Problem problem) throws IOException {

        /**
         * Write the test case input parameter variables at the top of the file.
         * Iterate through the test cases, and then each of the problem inputs;
         * these are used to construct the input parameter variables.
         */
        List<ProblemTestCase> testCases = problem.getTestCases();
        for (int testNum = 1; testNum <= testCases.size(); testNum++) {

            List<Object> parsedInputs = inputParser.parseTestCase(problem, testCases.get(testNum - 1));
            
            // Iterate through the entries of inputs, which hold name and type.
            for (int i = 0; i < problem.getProblemInputs().size(); i++) {
                ProblemInput input = problem.getProblemInputs().get(i);

                // Get instantiation to hold the input type.
                String instantiation = typeInstantiationToString(input.getType());

                // Get the input parameter variable name.
                String inputName = input.getName();

                // Get initialization of input from its type and content.
                String initialization = typeInitializationToString(input.getType(), parsedInputs.get(i));

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
            writer.write(String.format("\t\t\t%s solution%d = new Solution().solve(", outputType, testNum));
            
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

            // Print solution output, catch errors that arise from method call.
            writer.write(String.join("\n",
                String.format("\t\t\tSystem.out.println(\"Solution (%d):\");", testNum),
                String.format("\t\t\tSystem.out.println(solution%d);", testNum),
                "\t\t} catch (Exception e) {",
                String.format("\t\t\tSystem.out.println(\"Error (%d):\");", testNum),
                "\t\t\te.printStackTrace();",
                "\t\t}\n"
            ));
        }
    }

    @Override
    public void writeEndingBoilerplate(FileWriter writer) throws IOException {
        writer.write(String.join("\n",
            "\t}",
            "}\n"
        ));
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

package com.rocketden.tester.service.generators;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
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
public class PythonDriverGeneratorService implements DriverGeneratorService {
    @Override
    public void writeDriverFile(String fileDirectory, Problem problem) {
        // Open writer using try-with-resources.
        try (FileWriter writer = new FileWriter(fileDirectory)) {
            writeStartingBoilerplate(writer);

            // Import requires method name.
            writer.write("from Solution import multiplyDouble\n\n");
            writer.write("def main():\n\n");

            writeTestCases(writer, problem);
            writeExecuteTestCases(writer, problem);
            writeEndingBoilerplate(writer);
        } catch (IOException e) {
            throw new ApiException(DockerSetupError.WRITE_CODE_TO_DISK);
        }
    }

    @Override
    public void writeStartingBoilerplate(FileWriter writer) throws IOException {
        writer.write("import traceback\n");
        writer.write("import os\n");
        writer.write("os.chdir(os.getcwd())\n\n");
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
                 * Format: [input name][test num] = [input content];
                 * Ex: num1 = 5;
                 */       
                writer.write(
                    String.format("\t%s%d = %s;%n",
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
        // Instantiate and initialize solution class object to call user code.
        writer.write("\t\tSolution solution = new Solution();\n");

        // Execute each of the test cases within separate try-catch blocks.
        for (int testNum = 1; testNum <= problem.getTestCases().size(); testNum++) {
            // Print line to predict any console output.
            writer.write(String.format("\tprint('Console (%d):')%n", testNum));

            // Use try-catch block to print any errors.
            writer.write("\ttry:\n");

            // Write the base setup (w/o parameters) of calling user's solution.
            writer.write(String.format("\t\t\tsolution%d = multiplyDouble(", testNum));
            
            // Record the input (parameter) names for the function call.
            Iterator<Entry<String, ProblemIOType>> inputIterator = problem.getInputNameTypeMap().entrySet().iterator();
            while (inputIterator.hasNext()) {
                Entry<String, ProblemIOType> input = inputIterator.next();

                // Write the input (parameter) variable name.
                writer.write(String.format("%s%d", input.getKey(), testNum));

                // Add comma + space, if more inputs are present.
                if (inputIterator.hasNext()) {
                    writer.write(", ");
                }
            }

            // End the call of the user's function / code.
            writer.write(")\n");

            // Print line to predict, then print, any solution output.
            writer.write(String.format("\t\tprint('Solution (%d):')%n", testNum));
            writer.write(String.format("\t\tprint(solution%d)%n", testNum));

            // Catch and print any errors that arise from calling user's code.
            writer.write("\texcept Exception as e:\n");
            writer.write(String.format("\t\tprint('Error (%d):')%n", testNum));
            writer.write("\t\ttraceback.print_exc()\n");
        }
    }

    @Override
    public void writeEndingBoilerplate(FileWriter writer) throws IOException {
        writer.write("if __name__ == \"__main__\":\n");
        writer.write("\tmain()\n");
    }

    @Override
    public void writeToStringCode() {
        // TODO Auto-generated method stub

    }

    @Override
    public String typeInstantiationToString(ProblemIOType ioType) {
        // Python does not require type instantiation.
        return null;
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
                return String.format("[\"%s\"]", String.join("\", \"", (String[]) value));
            case ARRAY_INTEGER:
                return String.format("[%s]", StringUtils.join((Integer[]) value, ", "));
            case ARRAY_DOUBLE:
                return String.format("[%s]", StringUtils.join((Double[]) value, ", "));
            case ARRAY_CHARACTER:
                return String.format("['%s']", StringUtils.join((Character[]) value, "', '"));
            case ARRAY_BOOLEAN:
                return String.format("[%s]", StringUtils.join((Boolean[]) value, ", "));
            default:
                return "";
        }
    }
}

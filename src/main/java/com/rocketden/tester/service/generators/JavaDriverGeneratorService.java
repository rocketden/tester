package com.rocketden.tester.service.generators;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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
        Problem prob = new Problem();
        prob.setMethodName("multiplyDouble");

        Map<String, ProblemIOType> inputNameTypeMap = new HashMap<>();
        inputNameTypeMap.put("num", ProblemIOType.INTEGER);
        prob.setInputNameTypeMap(inputNameTypeMap);
        prob.setOutputType(ProblemIOType.INTEGER);

        List<ProblemTestCase> testCases = new ArrayList<>();
        ProblemTestCase tc1 = new ProblemTestCase();
        tc1.setInput("2");
        tc1.setOutput("4");

        ProblemTestCase tc2 = new ProblemTestCase();
        tc2.setInput("5");
        tc2.setOutput("10");

        ProblemTestCase tc3 = new ProblemTestCase();
        tc3.setInput("13");
        tc3.setOutput("26");

        testCases.add(tc1);
        testCases.add(tc2);
        testCases.add(tc3);

        prob.setTestCases(testCases);

        problem = prob;

        // Open writer using try-with-resources.
        try (FileWriter writer = new FileWriter(fileDirectory)) {
            // Boilerplate starting setup.
            writer.write("public class Driver {\n");
            writer.write("\tpublic static void main (String[] args) {\n");

            // Write test cases.
            // TODO: Write conversion from tc.getInput() to object.
            int testNum = 1;
            for (ProblemTestCase tc : problem.getTestCases()) {
                int inputNum = 1;
                for (Entry<String, ProblemIOType> input : problem.getInputNameTypeMap().entrySet()) {
                    writer.write(
                        String.format("\t\t%s test%d%s%d = %s;%n",
                            typeInstantiationToString(input.getValue()),
                            testNum,
                            input.getKey(),
                            inputNum,
                            typeInitializationToString(input.getValue(), Integer.parseInt(tc.getInput()))
                        )
                    );
                    inputNum++;
                }
                testNum++;
            }
            // writer.write("\t\tInteger test1var1 = 2;\n");
            // writer.write("\t\tInteger test2var1 = 5;\n");
            // writer.write("\t\tInteger test3var1 = 13;\n");

            // Get solutions from the user code.
            writer.write("\t\tSolution solution = new Solution();\n");

            // Write test cases.
            testNum = 1;
            String output = typeInstantiationToString(problem.getOutputType());
            for (ProblemTestCase tc : problem.getTestCases()) {
                writer.write(String.format("\t\tSystem.out.println(\"Console (%d):\");%n", testNum));
                // Use try-catch block to print any errors.
                writer.write("\t\ttry {\n");
                writer.write(String.format("\t\t\t%s solution%d = solution.multiplyDouble(", output, testNum));
                
                // Record multiple inputs.
                int inputNum = 1;
                for (Entry<String, ProblemIOType> input : problem.getInputNameTypeMap().entrySet()) {
                    writer.write(String.format("test%d%s%d", testNum,
                        input.getKey(), inputNum));

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

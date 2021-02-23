package com.rocketden.tester.util;

import com.rocketden.tester.model.problem.Problem;
import com.rocketden.tester.model.problem.ProblemIOType;
import com.rocketden.tester.model.problem.ProblemInput;
import com.rocketden.tester.model.problem.ProblemTestCase;

import java.util.ArrayList;
import java.util.List;

public class ProblemTestMethods {

    public static Problem getFindMaxProblem(String[]... inputs) {
        List<ProblemTestCase> testCases = new ArrayList<>();

        for (String[] input : inputs) {
            ProblemTestCase testCase = new ProblemTestCase();
            testCase.setInput(input[0]);
            testCase.setOutput(input[1]);
            testCases.add(testCase);
        }

        List<ProblemInput> problemInputs = new ArrayList<>();
        ProblemInput problemInput = new ProblemInput();
        problemInput.setType(ProblemIOType.ARRAY_INTEGER);
        problemInput.setName("array");
        problemInputs.add(problemInput);

        Problem problem = new Problem();
        problem.setTestCases(testCases);
        problem.setProblemInputs(problemInputs);
        problem.setOutputType(ProblemIOType.INTEGER);

        return problem;
    }

    public static Problem getSortStringArrayProblem(String[]... inputs) {
        List<ProblemTestCase> testCases = new ArrayList<>();

        for (String[] input : inputs) {
            ProblemTestCase testCase = new ProblemTestCase();
            testCase.setInput(input[0]);
            testCase.setOutput(input[1]);
            testCases.add(testCase);
        }

        List<ProblemInput> problemInputs = new ArrayList<>();
        ProblemInput problemInput = new ProblemInput();
        problemInput.setType(ProblemIOType.ARRAY_STRING);
        problemInput.setName("array");
        problemInputs.add(problemInput);

        Problem problem = new Problem();
        problem.setTestCases(testCases);
        problem.setProblemInputs(problemInputs);
        problem.setOutputType(ProblemIOType.ARRAY_STRING);

        return problem;
    }

    public static Problem getSumProblem(String[]... inputs) {
        List<ProblemTestCase> testCases = new ArrayList<>();

        for (String[] input : inputs) {
            ProblemTestCase testCase = new ProblemTestCase();
            testCase.setInput(input[0]);
            testCase.setOutput(input[1]);
            testCases.add(testCase);
        }

        List<ProblemInput> problemInputs = new ArrayList<>();
        ProblemInput problemInput = new ProblemInput();
        problemInput.setType(ProblemIOType.INTEGER);
        problemInput.setName("num1");
        problemInputs.add(problemInput);

        ProblemInput problemInput2 = new ProblemInput();
        problemInput2.setType(ProblemIOType.INTEGER);
        problemInput2.setName("num2");
        problemInputs.add(problemInput2);

        Problem problem = new Problem();
        problem.setTestCases(testCases);
        problem.setProblemInputs(problemInputs);
        problem.setOutputType(ProblemIOType.INTEGER);

        return problem;
    }

    public static Problem getMultiplyDoubleProblem(String[]... inputs) {
        List<ProblemTestCase> testCases = new ArrayList<>();

        for (String[] input : inputs) {
            ProblemTestCase testCase = new ProblemTestCase();
            testCase.setInput(input[0]);
            testCase.setOutput(input[1]);
            testCases.add(testCase);
        }

        List<ProblemInput> problemInputs = new ArrayList<>();
        ProblemInput problemInput = new ProblemInput();
        problemInput.setType(ProblemIOType.INTEGER);
        problemInput.setName("num");
        problemInputs.add(problemInput);

        Problem problem = new Problem();
        problem.setTestCases(testCases);
        problem.setProblemInputs(problemInputs);
        problem.setOutputType(ProblemIOType.INTEGER);

        return problem;
    }

    public static Problem getAllTypesProblem(String... inputs) {
        List<ProblemTestCase> testCases = new ArrayList<>();

        for (String input : inputs) {
            ProblemTestCase testCase = new ProblemTestCase();
            testCase.setInput(input);
            testCase.setOutput("0");
            testCases.add(testCase);
        }

        List<ProblemInput> problemInputs = new ArrayList<>();

        int count = 1;
        for (ProblemIOType type : ProblemIOType.values()) {
            ProblemInput problemInput = new ProblemInput();
            problemInput.setType(type);
            problemInput.setName(String.format("p%s", count));

            problemInputs.add(problemInput);
            count++;
        }

        Problem problem = new Problem();
        problem.setTestCases(testCases);
        problem.setProblemInputs(problemInputs);
        problem.setOutputType(ProblemIOType.INTEGER);

        return problem;
    }

    public static Problem getVariedReturnTypeProblem(ProblemIOType returnType, String input) {
        List<ProblemTestCase> testCases = new ArrayList<>();
        ProblemTestCase testCase = new ProblemTestCase();
        testCase.setInput(input);
        testCase.setOutput(input);
        testCases.add(testCase);

        List<ProblemInput> problemInputs = new ArrayList<>();
        ProblemInput problemInput = new ProblemInput();
        problemInput.setType(returnType);
        problemInput.setName("param");
        problemInputs.add(problemInput);

        Problem problem = new Problem();
        problem.setTestCases(testCases);
        problem.setProblemInputs(problemInputs);
        problem.setOutputType(returnType);

        return problem;
    }
}

package com.rocketden.tester.util;

import com.rocketden.tester.model.Language;
import com.rocketden.tester.model.problem.Problem;
import com.rocketden.tester.model.problem.ProblemIOType;
import com.rocketden.tester.model.problem.ProblemInput;
import com.rocketden.tester.model.problem.ProblemTestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProblemTestMethods {

    public static Problem getFindMaxProblem(String... inputs) {
        Map<Language, String> methodNames = Map.of(
                Language.JAVA, "findMax",
                Language.PYTHON, "find_max"
        );

        List<ProblemTestCase> testCases = new ArrayList<>();

        for (String input : inputs) {
            ProblemTestCase testCase = new ProblemTestCase();
            testCase.setInput(input);
            testCases.add(testCase);
        }

        List<ProblemInput> problemInputs = new ArrayList<>();
        ProblemInput problemInput = new ProblemInput();
        problemInput.setType(ProblemIOType.ARRAY_INTEGER);
        problemInput.setName("array");
        problemInputs.add(problemInput);

        Problem problem = new Problem();
        problem.setMethodNames(methodNames);
        problem.setTestCases(testCases);
        problem.setProblemInputs(problemInputs);
        problem.setOutputType(ProblemIOType.INTEGER);
        problem.setMethodNames(methodNames);

        return problem;
    }
}

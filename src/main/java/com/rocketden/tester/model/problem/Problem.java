package com.rocketden.tester.model.problem;

import java.util.List;
import java.util.Map;

import com.rocketden.tester.model.Language;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Problem {
    private List<ProblemTestCase> testCases;

    // Additional fields for the creation of the driver file.
    private Map<Language, String> methodNames;
    private List<ProblemInput> problemInputs;
    private ProblemIOType outputType;
}

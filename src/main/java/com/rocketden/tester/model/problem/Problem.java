package com.rocketden.tester.model.problem;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Problem {
    private Integer id;
    private String problemId;
    private String name;
    private String description;
    private List<ProblemTestCase> testCases;

    // Additional fields for the creation of the driver file.
    private List<ProblemInput> problemInputs;
    private ProblemIOType outputType;
}

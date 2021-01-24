package com.rocketden.tester.model.problem;

import java.util.ArrayList;
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
    private List<ProblemTestCase> testCases = new ArrayList<>();

    // Additional fields added necessary for the creation of the driver file.
    private String methodName;
}

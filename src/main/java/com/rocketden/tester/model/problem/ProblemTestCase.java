package com.rocketden.tester.model.problem;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProblemTestCase {
    private Integer id;
    private String input;
    private String output;
    private Boolean hidden;
    private Problem problem;
    private String explanation;
}
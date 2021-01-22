package com.rocketden.tester.dto;

import com.rocketden.tester.model.Language;
import com.rocketden.tester.model.problem.Problem;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RunRequest {
    private String code;
    private Language language; 
    private Problem problem;
}

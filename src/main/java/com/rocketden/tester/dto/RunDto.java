package com.rocketden.tester.dto;

import java.util.List;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RunDto {
    private List<ResultDto> results;
    private Integer numCorrect;
    private Integer numTestCases;
    private Long runtime;
}

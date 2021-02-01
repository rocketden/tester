package com.rocketden.tester.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.rocketden.tester.model.Language;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RunDto {
    private List<ResultDto> results;
    private String code;
    private Language language;
    private Integer numCorrect;
    private Integer numTestCases;
    private Long runtime;
    private LocalDateTime startTime;
}

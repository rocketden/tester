package com.rocketden.tester.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultDto {
    private String console;
    private String userOutput;
    private String error;
    private String correctOutput;
    private boolean correct;
}

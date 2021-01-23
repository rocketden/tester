package com.rocketden.tester.dto;

import com.rocketden.tester.model.Language;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RunRequest {
    private String code;
    private Language language; 
}

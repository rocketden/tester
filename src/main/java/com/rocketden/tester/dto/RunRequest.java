package com.rocketden.tester.dto;

import lombok.Getter;
import lombok.Setter;

//@Getter
//@Setter
public class RunRequest {
    private String code;
    private String language; 

    public String getCode(){
        return code;
    }

    public String getLanguage(){
        return language;
    }
}

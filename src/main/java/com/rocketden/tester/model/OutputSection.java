package com.rocketden.tester.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.rocketden.tester.exception.ParserError;
import com.rocketden.tester.exception.api.ApiException;

public enum OutputSection {

    TEST_CASE,
    SUCCESS,
    FAILURE,
    START;

    // Convert a matching string (ignoring case) to enum object
    @JsonCreator
    public static Language fromString(String value) {
        try {
            return Language.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ApiException(ParserError.BAD_SECTION);
        }
    }
    
}

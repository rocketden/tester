package com.rocketden.tester.model.problem;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.rocketden.tester.exception.ProblemError;
import com.rocketden.tester.exception.api.ApiException;

public enum ProblemDifficulty {
    EASY, MEDIUM, HARD, RANDOM;

    // Convert a matching string (ignoring case) to enum object
    @JsonCreator
    public static ProblemDifficulty fromString(String value) {
        try {
            return ProblemDifficulty.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ApiException(ProblemError.BAD_DIFFICULTY);
        }
    }
}

package com.rocketden.tester.exception;

import com.rocketden.tester.exception.api.ApiError;
import com.rocketden.tester.exception.api.ApiErrorResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ParserError implements ApiError {

    INCORRECT_COUNT(HttpStatus.BAD_REQUEST, "Please specify the correct number of inputs for ths problem."),
    INVALID_FORMAT(HttpStatus.BAD_REQUEST, "Please enter each line of your input in a valid format."),
    INVALID_TYPE(HttpStatus.BAD_REQUEST, "The type you provided does not match the specified type for this problem.");

    private final HttpStatus status;
    private final ApiErrorResponse response;

    ParserError(HttpStatus status, String message) {
        this.status = status;
        this.response = new ApiErrorResponse(message, this.name());
    }

    public String getMessage() {
        return this.response.getMessage();
    }
}

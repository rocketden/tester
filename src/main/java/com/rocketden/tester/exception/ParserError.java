package com.rocketden.tester.exception;

import com.rocketden.tester.exception.api.ApiError;
import com.rocketden.tester.exception.api.ApiErrorResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ParserError implements ApiError {

    INCORRECT_COUNT(HttpStatus.BAD_REQUEST, "Please specify the correct number of inputs for ths problem."),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "Please ensure each line of input is valid and is of the correct type."),
    BAD_SECTION(HttpStatus.BAD_REQUEST, "Please choose a valid ouptut section."),
    MISFORMATTED_OUTPUT(HttpStatus.INTERNAL_SERVER_ERROR, "The output from the user's code is misformatted, and thus cannot be parsed accurately.");

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

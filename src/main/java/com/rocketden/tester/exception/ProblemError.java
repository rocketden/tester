package com.rocketden.tester.exception;

import com.rocketden.tester.exception.api.ApiError;
import com.rocketden.tester.exception.api.ApiErrorResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ProblemError implements ApiError {

    BAD_IOTYPE(HttpStatus.BAD_REQUEST, "Please choose a value Problem IO Type."),
    BAD_PARAMETER_SETTINGS(HttpStatus.BAD_REQUEST, "Please ensure each problem parameter has a valid name and type."),
    OBJECT_MATCH_IOTYPE(HttpStatus.BAD_REQUEST, "The provided object does not match the specified Problem IO Type.");

    private final HttpStatus status;
    private final ApiErrorResponse response;

    ProblemError(HttpStatus status, String message) {
        this.status = status;
        this.response = new ApiErrorResponse(message, this.name());
    }

    public String getMessage() {
        return this.response.getMessage();
    }
}

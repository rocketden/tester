package com.rocketden.tester.exception;

import com.rocketden.tester.exception.api.ApiError;
import com.rocketden.tester.exception.api.ApiErrorResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ProblemError implements ApiError {

    BAD_DIFFICULTY(HttpStatus.BAD_REQUEST, "Please choose either Easy, Medium, or Hard (or Random if choosing a room difficulty).");

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
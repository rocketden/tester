package com.rocketden.tester.exception;

import com.rocketden.tester.exception.api.ApiError;
import com.rocketden.tester.exception.api.ApiErrorResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum LanguageError implements ApiError {

    BAD_LANGUAGE(HttpStatus.BAD_REQUEST, "Please choose a valid language supported by this program.");

    private final HttpStatus status;
    private final ApiErrorResponse response;

    LanguageError(HttpStatus status, String message) {
        this.status = status;
        this.response = new ApiErrorResponse(message, this.name());
    }

    public String getMessage() {
        return this.response.getMessage();
    }
}

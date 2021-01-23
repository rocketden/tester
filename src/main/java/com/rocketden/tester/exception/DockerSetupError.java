package com.rocketden.tester.exception;

import com.rocketden.tester.exception.api.ApiError;
import com.rocketden.tester.exception.api.ApiErrorResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum DockerSetupError implements ApiError {

    WRITE_USER_CODE(HttpStatus.INTERNAL_SERVER_ERROR, "The program unexpectedly failed to write user code to the disk."),
    CREATE_TEMP_FOLDER(HttpStatus.INTERNAL_SERVER_ERROR, "The program unexpectedly failed to create a temp folder for this request."),
    DELETE_TEMP_FOLDER(HttpStatus.INTERNAL_SERVER_ERROR, "The program unexpectedly failed to delete the temp folder for this request."),
    BUILD_DOCKER_CONTAINER(HttpStatus.INTERNAL_SERVER_ERROR, "The program unexpectedly failed to create a docker container.");

    private final HttpStatus status;
    private final ApiErrorResponse response;

    DockerSetupError(HttpStatus status, String message) {
        this.status = status;
        this.response = new ApiErrorResponse(message, this.name());
    }

    public String getMessage() {
        return this.response.getMessage();
    }
}

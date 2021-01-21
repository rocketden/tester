package com.rocketden.tester.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.rocketden.tester.exception.LanguageError;
import com.rocketden.tester.exception.api.ApiException;

import lombok.Getter;

@Getter
public enum Language {

    PYTHON("docker_python", "py"), RUBY("docker_ruby", "rb"),
    SWIFT("docker_swift", "swift"), CPP("docker_cpp", "cpp"),
    PHP("docker_php", "php"), C("docker_c", "c"), JAVA("docker_java", "java"),
    RUST("docker_rust", "rs"), BASH("docker_bash", "sh");

    private final String dockerContainer;
    private final String extension;

    Language(String dockerContainer, String extension) {
        this.dockerContainer = dockerContainer;
        this.extension = extension;
    }

    // Convert a matching string (ignoring case) to enum object
    @JsonCreator
    public static Language fromString(String value) {
        try {
            return Language.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ApiException(LanguageError.BAD_LANGUAGE);
        }
    }
}

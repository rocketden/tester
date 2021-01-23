package com.rocketden.tester.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.rocketden.tester.exception.LanguageError;
import com.rocketden.tester.exception.api.ApiException;

import lombok.Getter;

@Getter
public enum Language {

    PYTHON("py"),
    RUBY("rb"),
    SWIFT("swift"),
    CPP("cpp"),
    PHP("php"),
    C("c"),
    JAVA("java"),
    JAVASCRIPT("js"),
    RUST("rs"),
    BASH("sh");

    private final String dockerContainer;
    private final String extension;

    Language(String extension) {
        this.dockerContainer = String.format("docker_%s", this.name().toLowerCase());
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

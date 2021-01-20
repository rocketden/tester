package com.rocketden.tester.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.rocketden.tester.exception.LanguageError;
import com.rocketden.tester.exception.api.ApiException;

import lombok.Getter;

@Getter
public enum Language {

    PYTHON("python", "py"), RUBY("ruby", "rb"), SWIFT("swift", "swift"),
    CPP("cpp", "cpp"), PHP("php", "php"), C("gcc", "c"), JAVA("java", "java"),
    RUST("rust", "rs"), BASH("bash", "sh");

    private final String path;
    private final String extension;

    Language(String path, String extension) {
        this.path = path;
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

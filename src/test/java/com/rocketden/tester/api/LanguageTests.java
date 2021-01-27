package com.rocketden.tester.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.rocketden.tester.exception.LanguageError;
import com.rocketden.tester.exception.api.ApiException;
import com.rocketden.tester.model.Language;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LanguageTests {
    @Test
    public void languageSuccess() {
        assertEquals(Language.JAVA, Language.fromString("java"));
    }
    
    @Test
    public void languageBadType() {
        ApiException exception = assertThrows(ApiException.class, () ->
            Language.fromString("nonexistentlanguage"));
        assertEquals(LanguageError.BAD_LANGUAGE, exception.getError());
        ;
    }
}
package com.rocketden.tester.api;

import com.rocketden.tester.exception.LanguageError;
import com.rocketden.tester.exception.api.ApiError;
import com.rocketden.tester.exception.api.ApiErrorResponse;
import com.rocketden.tester.util.UtilityTestMethods;
import com.rocketden.tester.dto.RunDto;
import com.rocketden.tester.dto.RunRequest;
import com.rocketden.tester.model.Language;
import com.rocketden.tester.model.problem.Problem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RunnerTests {

    @Autowired
    private MockMvc mockMvc;

    private static final String POST_RUNNER = "/api/v1/runner";

    @Test
    public void runRequestBadLanguage() throws Exception {
        String jsonRequest = "{\"code\": \"print('hi')\", \"language\": \"nonexistent\", \"problem\": \"{}\"}";

        ApiError ERROR = LanguageError.BAD_LANGUAGE;

        MvcResult result = this.mockMvc.perform(post(POST_RUNNER)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonRequest))
                .andDo(print()).andExpect(status().is(ERROR.getStatus().value()))
                .andReturn();

        String response = result.getResponse().getContentAsString();
        ApiErrorResponse actual = UtilityTestMethods.toObject(response, ApiErrorResponse.class);

        assertEquals(ERROR.getResponse(), actual);
    }
}

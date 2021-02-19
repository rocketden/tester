package com.rocketden.tester.api;

import com.rocketden.tester.exception.LanguageError;
import com.rocketden.tester.exception.ParserError;
import com.rocketden.tester.exception.ProblemError;
import com.rocketden.tester.exception.RequestError;
import com.rocketden.tester.exception.api.ApiError;
import com.rocketden.tester.exception.api.ApiErrorResponse;
import com.rocketden.tester.util.ProblemTestMethods;
import com.rocketden.tester.util.UtilityTestMethods;
import com.rocketden.tester.dto.RunRequest;
import com.rocketden.tester.model.Language;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RunnerTests {

    @Autowired
    private MockMvc mockMvc;

    private static final String POST_RUNNER = "/api/v1/runner";

    private static final Language LANGUAGE = Language.PYTHON;
    private static final String CODE = "print('hi')";

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

    @Test
    public void runRequestMissingBaseField() throws Exception {
        RunRequest request = new RunRequest();
        request.setLanguage(LANGUAGE);
        request.setCode(CODE);

        ApiError ERROR = RequestError.EMPTY_FIELD;

        MvcResult result = this.mockMvc.perform(post(POST_RUNNER)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(UtilityTestMethods.convertObjectToJsonString(request)))
                .andDo(print()).andExpect(status().is(ERROR.getStatus().value()))
                .andReturn();

        String response = result.getResponse().getContentAsString();
        ApiErrorResponse actual = UtilityTestMethods.toObject(response, ApiErrorResponse.class);

        assertEquals(ERROR.getResponse(), actual);
    }

    @Test
    public void runRequestMissingProblemField() throws Exception {
        RunRequest request = new RunRequest();
        request.setLanguage(LANGUAGE);
        request.setCode(CODE);
        request.setProblem(ProblemTestMethods.getFindMaxProblem(new String[]{"[1]", "1"}));
        request.getProblem().setOutputType(null);

        ApiError ERROR = RequestError.EMPTY_FIELD;

        MvcResult result = this.mockMvc.perform(post(POST_RUNNER)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(UtilityTestMethods.convertObjectToJsonString(request)))
                .andDo(print()).andExpect(status().is(ERROR.getStatus().value()))
                .andReturn();

        String response = result.getResponse().getContentAsString();
        ApiErrorResponse actual = UtilityTestMethods.toObject(response, ApiErrorResponse.class);

        assertEquals(ERROR.getResponse(), actual);
    }

    @Test
    public void runRequestEmptyTestCases() throws Exception {
        RunRequest request = new RunRequest();
        request.setLanguage(LANGUAGE);
        request.setCode(CODE);
        request.setProblem(ProblemTestMethods.getFindMaxProblem());

        ApiError ERROR = RequestError.EMPTY_FIELD;

        MvcResult result = this.mockMvc.perform(post(POST_RUNNER)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(UtilityTestMethods.convertObjectToJsonString(request)))
                .andDo(print()).andExpect(status().is(ERROR.getStatus().value()))
                .andReturn();

        String response = result.getResponse().getContentAsString();
        ApiErrorResponse actual = UtilityTestMethods.toObject(response, ApiErrorResponse.class);

        assertEquals(ERROR.getResponse(), actual);
    }

    @Test
    public void runRequestMissingProblemInputField() throws Exception {
        RunRequest request = new RunRequest();
        request.setLanguage(LANGUAGE);
        request.setCode(CODE);
        request.setProblem(ProblemTestMethods.getFindMaxProblem(new String[]{"[1]", "1"}));
        request.getProblem().getProblemInputs().get(0).setName("");

        ApiError ERROR = ProblemError.BAD_PARAMETER_SETTINGS;

        MvcResult result = this.mockMvc.perform(post(POST_RUNNER)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(UtilityTestMethods.convertObjectToJsonString(request)))
                .andDo(print()).andExpect(status().is(ERROR.getStatus().value()))
                .andReturn();

        String response = result.getResponse().getContentAsString();
        ApiErrorResponse actual = UtilityTestMethods.toObject(response, ApiErrorResponse.class);

        assertEquals(ERROR.getResponse(), actual);
    }

    @Test
    public void runRequestBadTestCaseInputs() throws Exception {
        RunRequest request = new RunRequest();
        request.setLanguage(LANGUAGE);
        request.setCode(CODE);
        request.setProblem(ProblemTestMethods.getFindMaxProblem(new String[]{"[1, 2, hi]", "2"}));

        ApiError ERROR = ParserError.INVALID_INPUT;

        MvcResult result = this.mockMvc.perform(post(POST_RUNNER)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(UtilityTestMethods.convertObjectToJsonString(request)))
                .andDo(print()).andExpect(status().is(ERROR.getStatus().value()))
                .andReturn();

        String response = result.getResponse().getContentAsString();
        ApiErrorResponse actual = UtilityTestMethods.toObject(response, ApiErrorResponse.class);

        assertEquals(ERROR.getResponse(), actual);
    }

    @Test
    public void runRequestBadTestCaseIncorrectInputCount() throws Exception {
        RunRequest request = new RunRequest();
        request.setLanguage(LANGUAGE);
        request.setCode(CODE);
        request.setProblem(ProblemTestMethods.getSumProblem(new String[]{"5\n", "5"}));

        ApiError ERROR = ParserError.INCORRECT_COUNT;

        MvcResult result = this.mockMvc.perform(post(POST_RUNNER)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(UtilityTestMethods.convertObjectToJsonString(request)))
                .andDo(print()).andExpect(status().is(ERROR.getStatus().value()))
                .andReturn();

        String response = result.getResponse().getContentAsString();
        ApiErrorResponse actual = UtilityTestMethods.toObject(response, ApiErrorResponse.class);

        assertEquals(ERROR.getResponse(), actual);
    }

    @Test
    public void runRequestCompilationError() throws Exception {
        // TODO for Java
    }

    @Test
    public void runRequestMisformattedOutput() throws Exception {
        // TODO: misformatted input ones (in output parse)
    }
}

package com.rocketden.tester.api;

import com.rocketden.tester.util.ProblemTestMethods;
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
class JavaTests {

    @Autowired
    private MockMvc mockMvc;

    private static final String POST_RUNNER = "/api/v1/runner";

    private static final Language LANGUAGE = Language.JAVA;
    private static final String CODE = String.join("\n",
            "import java.util.Arrays;",
            "",
            "public class Solution {",
            "    public int solve(int[] array) {",
            "        return Arrays.stream(array).max().getAsInt();",
            "    }",
            "}");

    @Test
    public void runRequestSuccess() throws Exception {
        RunRequest request = new RunRequest();
        request.setCode(CODE);
        request.setLanguage(LANGUAGE);

        Problem problem = ProblemTestMethods.getFindMaxProblem("[1, 3, 5, 7, 4, 2]", "[-5, 16, 0]");
        request.setProblem(problem);

        MvcResult result = this.mockMvc.perform(post(POST_RUNNER)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(UtilityTestMethods.convertObjectToJsonString(request)))
                .andDo(print()).andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        RunDto runDto = UtilityTestMethods.toObject(response, RunDto.class);

        String expected = String.join("\n",
                "Console (1):",
                "Solution (1):",
                "7",
                "Console (2):",
                "Solution (2):",
                "16",
                "");

        assertTrue(runDto.isStatus());
        assertEquals(expected, runDto.getOutput());
    }

    @Test
    public void runRequestSuccessMultipleParams() throws Exception {
        String code = String.join("\n",
                "public class Solution {",
                "    public int solve(int num1, int num2) {",
                "        return num1 + num2;",
                "    }",
                "}");

        RunRequest request = new RunRequest();
        request.setCode(code);
        request.setLanguage(LANGUAGE);

        Problem problem = ProblemTestMethods.getSumProblem("2\n3\n");
        request.setProblem(problem);

        MvcResult result = this.mockMvc.perform(post(POST_RUNNER)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(UtilityTestMethods.convertObjectToJsonString(request)))
                .andDo(print()).andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        RunDto runDto = UtilityTestMethods.toObject(response, RunDto.class);

        String expected = String.join("\n",
                "Console (1):",
                "Solution (1):",
                "5",
                "");

        assertTrue(runDto.isStatus());
        assertEquals(expected, runDto.getOutput());
    }

    @Test
    public void runRequestWrongAnswer() throws Exception {
        // TODO
    }

    @Test
    public void runRequestErrorOccurred() throws Exception {
        // TODO - test error portions
    }

    @Test
    public void runRequestConsoleOutput() throws Exception {
        // TODO - test console output portions
    }
}

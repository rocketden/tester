package com.rocketden.tester.api;

import com.rocketden.tester.util.ProblemTestMethods;
import com.rocketden.tester.util.UtilityTestMethods;
import com.rocketden.tester.dto.ResultDto;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
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
            "        System.out.println(\"Test print.\");",
            "        return Arrays.stream(array).max().getAsInt();",
            "    }",
            "}");

    @Test
    public void runRequestSuccess() throws Exception {
        RunRequest request = new RunRequest();
        request.setCode(CODE);
        request.setLanguage(LANGUAGE);

        Problem problem = ProblemTestMethods.getFindMaxProblem(new String[]{"[1, 3, 5, 7, 4, 2]", "7"}, new String[]{"[-5, 16, 0]", "16"});
        request.setProblem(problem);

        MvcResult result = this.mockMvc.perform(post(POST_RUNNER)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(UtilityTestMethods.convertObjectToJsonString(request)))
                .andDo(print()).andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        RunDto runDto = UtilityTestMethods.toObject(response, RunDto.class);

        // Check the base fields of runDto.
        assertEquals(2, runDto.getNumCorrect());
        assertEquals(2, runDto.getNumTestCases());
        assertEquals(0.0, runDto.getRuntime());

        // Check the individual results within the runDto.
        assertEquals(2, runDto.getResults().size());

        // Check the first result.
        ResultDto resultDto = runDto.getResults().get(0);
        assertEquals("Test print.\n", resultDto.getConsole());
        assertEquals("7\n", resultDto.getUserOutput());
        assertNull(resultDto.getError());
        assertEquals("7", resultDto.getCorrectOutput());
        assertTrue(resultDto.isCorrect());

        // Check the second result.
        resultDto = runDto.getResults().get(1);
        assertEquals("Test print.\n", resultDto.getConsole());
        assertEquals("16\n", resultDto.getUserOutput());
        assertNull(resultDto.getError());
        assertEquals("16", resultDto.getCorrectOutput());
        assertTrue(resultDto.isCorrect());
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

        Problem problem = ProblemTestMethods.getSumProblem(new String[]{"2\n3\n", "5"});
        request.setProblem(problem);

        MvcResult result = this.mockMvc.perform(post(POST_RUNNER)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(UtilityTestMethods.convertObjectToJsonString(request)))
                .andDo(print()).andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        RunDto runDto = UtilityTestMethods.toObject(response, RunDto.class);

        // Check the base fields of runDto.
        assertEquals(1, runDto.getNumCorrect());
        assertEquals(1, runDto.getNumTestCases());
        assertEquals(0.0, runDto.getRuntime());

        // Check the individual results within the runDto.
        assertEquals(1, runDto.getResults().size());

        // Check the first result.
        ResultDto resultDto = runDto.getResults().get(0);
        assertEquals("", resultDto.getConsole());
        assertEquals("5\n", resultDto.getUserOutput());
        assertNull(resultDto.getError());
        assertEquals("5", resultDto.getCorrectOutput());
        assertTrue(resultDto.isCorrect());
    }

    @Test
    public void runRequestCorrectAnswerStringArray() throws Exception {
        // Code to sort a string array, include import to use Arrays.sort.
        String code = String.join("\n",
        "import java.util.*;",
        "",
        "public class Solution {",
        "    public String[] solve(String[] array) {",
        "        if (array.length == 0) {",
        "            System.out.println(\"Input array is empty.\");",
        "        }",
        "        Arrays.sort(array);",
        "        return array;",
        "    }",
        "}");

        RunRequest request = new RunRequest();
        request.setCode(code);
        request.setLanguage(LANGUAGE);

        Problem problem = ProblemTestMethods.getSortStringArrayProblem(new String[]{"[\"notebook\", \"journal\", \"phone\", \"alphabetical\"]", "[\"alphabetical\", \"journal\", \"notebook\", \"phone\"]"}, new String[]{"[]", "[]"});
        request.setProblem(problem);

        MvcResult result = this.mockMvc.perform(post(POST_RUNNER)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(UtilityTestMethods.convertObjectToJsonString(request)))
                .andDo(print()).andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        RunDto runDto = UtilityTestMethods.toObject(response, RunDto.class);

        // Check the base fields of runDto.
        assertEquals(2, runDto.getNumCorrect());
        assertEquals(2, runDto.getNumTestCases());
        assertEquals(0.0, runDto.getRuntime());

        // Check the individual results within the runDto.
        assertEquals(2, runDto.getResults().size());

        // Check the first result.
        ResultDto resultDto = runDto.getResults().get(0);
        assertEquals("", resultDto.getConsole());
        assertEquals("[\"alphabetical\", \"journal\", \"notebook\", \"phone\"]\n", resultDto.getUserOutput());
        assertNull(resultDto.getError());
        assertEquals("[\"alphabetical\", \"journal\", \"notebook\", \"phone\"]", resultDto.getCorrectOutput());
        assertTrue(resultDto.isCorrect());

        // Check the second result.
        resultDto = runDto.getResults().get(1);
        assertEquals("Input array is empty.\n", resultDto.getConsole());
        assertEquals("[]\n", resultDto.getUserOutput());
        assertNull(resultDto.getError());
        assertEquals("[]", resultDto.getCorrectOutput());
        assertTrue(resultDto.isCorrect());
    }

    @Test
    public void runRequestWrongAnswer() throws Exception {
        // Create code that returns wrong answer for 2 + 3.
        String code = String.join("\n",
        "public class Solution {",
        "    public int solve(int num1, int num2) {",
        "        if (num1 == 2 && num2 == 3) {",
        "            return -1;",
        "        }",
        "        return num1 + num2;",
        "    }",
        "}");

        RunRequest request = new RunRequest();
        request.setCode(code);
        request.setLanguage(LANGUAGE);

        Problem problem = ProblemTestMethods.getSumProblem(new String[]{"2\n3\n", "5"}, new String[]{"5\n6\n", "11"});
        request.setProblem(problem);

        MvcResult result = this.mockMvc.perform(post(POST_RUNNER)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(UtilityTestMethods.convertObjectToJsonString(request)))
                .andDo(print()).andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        RunDto runDto = UtilityTestMethods.toObject(response, RunDto.class);

        // Check the base fields of runDto.
        assertEquals(1, runDto.getNumCorrect());
        assertEquals(2, runDto.getNumTestCases());
        assertEquals(0.0, runDto.getRuntime());

        // Check the individual results within the runDto.
        assertEquals(2, runDto.getResults().size());

        // Check the first result.
        ResultDto resultDto = runDto.getResults().get(0);
        assertEquals("", resultDto.getConsole());
        assertEquals("-1\n", resultDto.getUserOutput());
        assertNull(resultDto.getError());
        assertEquals("5", resultDto.getCorrectOutput());
        assertFalse(resultDto.isCorrect());

        // Check the second result.
        resultDto = runDto.getResults().get(1);
        assertEquals("", resultDto.getConsole());
        assertEquals("11\n", resultDto.getUserOutput());
        assertNull(resultDto.getError());
        assertEquals("11", resultDto.getCorrectOutput());
        assertTrue(resultDto.isCorrect());
    }

    @Test
    public void runRequestErrorOccurred() throws Exception {
        // TODO - test error portions
    }

    @Test
    public void runRequestConsoleOutputErrorOccurred() throws Exception {
        // TODO - test console output portions
    }
}

package com.rocketden.tester.api;

import com.rocketden.tester.model.problem.ProblemIOType;
import com.rocketden.tester.service.parsers.OutputParser;
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
class PythonTests {

    @Autowired
    private MockMvc mockMvc;

    private static final String POST_RUNNER = "/api/v1/runner";

    private static final Language LANGUAGE = Language.PYTHON;
    private static final String CODE = String.join("\n",
        "class Solution:",
        "    def solve(array):",
        "        print('Test print.')",
        "        return max(array)"
    );

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
        assertNull(runDto.getCompilationError());

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
            "class Solution(object):",
            "    def solve(num1, num2):",
            "        return num1 + num2"
        );

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
        assertNull(runDto.getCompilationError());

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
            "class Solution(object):",
            "    def solve(array):",
            "        if not array:",
            "            print('Input array is empty.')",
            "        array.sort()",
            "        return array"
        );

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
        // User output does not include \" for Strings.
        assertEquals("['alphabetical', 'journal', 'notebook', 'phone']\n", resultDto.getUserOutput());
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
            "class Solution(object):",
            "    def solve(num1, num2):",
            "        if num1 == 2 and num2 == 3:",
            "            return -1",
            "        return num1 + num2"
        );

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
        // Create code that returns error for the second and third problems.
        String code = String.join("\n",
            "class Solution(object):",
            "    def solve(num):",
            "        list = [1, 2, 4]",
            "        if num == 13:",
            "            print('Input array is empty.')",
            "        return list[num]"
        );

        RunRequest request = new RunRequest();
		request.setCode(code);
		request.setLanguage(LANGUAGE);

		Problem problem = ProblemTestMethods.getMultiplyDoubleProblem(new String[]{"2", "4"}, new String[]{"5", "10"}, new String[]{"13", "26"});
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
        assertEquals(3, runDto.getNumTestCases());
        assertEquals(0.0, runDto.getRuntime());

        // Check the individual results within the runDto.
        assertEquals(3, runDto.getResults().size());

        // Check the first result.
        ResultDto resultDto = runDto.getResults().get(0);
        assertEquals("", resultDto.getConsole());
        assertEquals("4\n", resultDto.getUserOutput());
        assertNull(resultDto.getError());
        assertEquals("4", resultDto.getCorrectOutput());
        assertTrue(resultDto.isCorrect());

        // Check the second result.
        resultDto = runDto.getResults().get(1);
        String expectedError = String.join("\n",
                "Traceback (most recent call last):",
                "  File \"/code/./Driver.py\", line 26, in main",
                "    solution2 = solution.solve(num2)",
                "  File \"/code/Solution.py\", line 6, in solve",
                "    return list[num]",
                "IndexError: list index out of range",
                ""
        );
        assertEquals("", resultDto.getConsole());
        assertNull(resultDto.getUserOutput());
        assertEquals(expectedError, resultDto.getError());
        assertEquals("10", resultDto.getCorrectOutput());
        assertFalse(resultDto.isCorrect());

        // Check the third result.
        resultDto = runDto.getResults().get(2);
        expectedError = String.join("\n",
                "Traceback (most recent call last):",
                "  File \"/code/./Driver.py\", line 35, in main",
                "    solution3 = solution.solve(num3)",
                "  File \"/code/Solution.py\", line 6, in solve",
                "    return list[num]",
                "IndexError: list index out of range",
                ""
        );
        assertEquals("Input array is empty.\n", resultDto.getConsole());
        assertNull(resultDto.getUserOutput());
        assertEquals(expectedError, resultDto.getError());
        assertEquals("26", resultDto.getCorrectOutput());
        assertFalse(resultDto.isCorrect());
    }

	@Test
	public void runRequestTestAllParameterTypes() throws Exception {
		String code = String.join("\n",
				"class Solution:",
				"    def solve(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10):",
				"        print(p1)",
				"        print(p2)",
				"        print(p3)",
				"        print(p4)",
				"        print(p5)",
				"        print(p6)",
				"        print(p7)",
				"        print(p8)",
				"        print(p9)",
				"        print(p10)",
				"",
				"        return 0");

		RunRequest request = new RunRequest();
		request.setCode(code);
		request.setLanguage(LANGUAGE);

		// Note: this array should match the order of the enums in ProblemIOType
		String[] inputs = {"p1", "2", "3.0", "4", "True", "['p6']", "[7]", "[8.0]", "['9']", "[False]"};

		Problem problem = ProblemTestMethods.getAllTypesProblem(String.join("\n", inputs));
		request.setProblem(problem);

		MvcResult result = this.mockMvc.perform(post(POST_RUNNER)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(UtilityTestMethods.convertObjectToJsonString(request)))
				.andDo(print()).andExpect(status().isOk())
				.andReturn();

		String response = result.getResponse().getContentAsString();
		RunDto runDto = UtilityTestMethods.toObject(response, RunDto.class);

		String expected = String.join("\n",
				inputs[0], inputs[1], inputs[2], inputs[3], inputs[4],
				inputs[5], inputs[6], inputs[7], inputs[8], inputs[9],
				"");

        assertEquals(1, runDto.getResults().size());

        ResultDto resultDto = runDto.getResults().get(0);
        assertEquals(expected, resultDto.getConsole());
	}

	@Test
	public void runRequestTestAllReturnTypes() throws Exception {
		String code = String.join("\n",
				"class Solution:",
				"    def solve(param):",
				"        return param");

		String[] inputs = {"p1", "2", "3.0", "4", "True", "['p6']", "[7]", "[8.0]", "['9']", "[False]"};
		int index = 0;
		for (ProblemIOType type : ProblemIOType.values()) {
			RunRequest request = new RunRequest();
			request.setCode(code);
			request.setLanguage(LANGUAGE);

			Problem problem = ProblemTestMethods.getVariedReturnTypeProblem(type, inputs[index]);
			request.setProblem(problem);

			MvcResult result = this.mockMvc.perform(post(POST_RUNNER)
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(UtilityTestMethods.convertObjectToJsonString(request)))
					.andDo(print()).andExpect(status().isOk())
					.andReturn();

			String response = result.getResponse().getContentAsString();
			RunDto runDto = UtilityTestMethods.toObject(response, RunDto.class);

            assertEquals(1, runDto.getResults().size());

            ResultDto resultDto = runDto.getResults().get(0);
            assertEquals(inputs[index] + "\n", resultDto.getUserOutput());
            assertEquals(inputs[index], resultDto.getCorrectOutput());

			index++;
		}
	}

    @Test
    public void runRequestRuntimeError() throws Exception {
        String code = String.join("\n",
                "class Solution:",
                "    def wrong(num1, num2):",
                "        return num1 + num2"
        );

        String error = "Traceback (most recent call last):\n" +
                "  File \"/code/./Driver.py\", line 16, in main\n" +
                "    solution1 = solution.solve(num11, num21)\n" +
                "AttributeError: type object 'Solution' has no attribute 'solve'\n";

        RunRequest request = new RunRequest();
        request.setCode(code);
        request.setLanguage(LANGUAGE);

        Problem problem = ProblemTestMethods.getSumProblem(new String[]{"1\n2", "3"});
        request.setProblem(problem);

        MvcResult result = this.mockMvc.perform(post(POST_RUNNER)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(UtilityTestMethods.convertObjectToJsonString(request)))
                .andDo(print()).andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        RunDto runDto = UtilityTestMethods.toObject(response, RunDto.class);

        assertEquals(1, runDto.getResults().size());
        assertEquals(0, runDto.getNumCorrect());
        assertEquals(problem.getTestCases().size(), runDto.getNumTestCases());
        assertNull(runDto.getCompilationError());

        ResultDto resultDto = runDto.getResults().get(0);

        assertEquals("3", resultDto.getCorrectOutput());
        assertEquals(error, resultDto.getError());
        assertNull(resultDto.getUserOutput());
    }

    @Test
    public void runRequestWrongReturnType() throws Exception {
        String code = String.join("\n",
                "class Solution:",
                "    def solve(num1, num2):",
                "        return 'foo'"
        );

        String error = OutputParser.WRONG_RETURN_TYPE;

        RunRequest request = new RunRequest();
        request.setCode(code);
        request.setLanguage(LANGUAGE);

        Problem problem = ProblemTestMethods.getSumProblem(new String[]{"1\n2", "3"});
        request.setProblem(problem);

        MvcResult result = this.mockMvc.perform(post(POST_RUNNER)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(UtilityTestMethods.convertObjectToJsonString(request)))
                .andDo(print()).andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        RunDto runDto = UtilityTestMethods.toObject(response, RunDto.class);

        assertEquals(1, runDto.getResults().size());
        assertEquals(0, runDto.getNumCorrect());
        assertEquals(problem.getTestCases().size(), runDto.getNumTestCases());
        assertNull(runDto.getCompilationError());

        ResultDto resultDto = runDto.getResults().get(0);

        assertEquals("3", resultDto.getCorrectOutput());
        assertEquals("foo\n", resultDto.getUserOutput());
        assertEquals(error, resultDto.getError());
    }
}

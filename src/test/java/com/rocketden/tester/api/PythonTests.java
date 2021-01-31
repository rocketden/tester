package com.rocketden.tester.api;

import com.rocketden.tester.service.parsers.OutputParser;
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
class PythonTests {

	@Autowired
	private MockMvc mockMvc;

	private static final String POST_RUNNER = "/api/v1/runner";

	private static final Language LANGUAGE = Language.PYTHON;
	private static final String CODE = String.join("\n",
			"class Solution:",
			"    def solve(array):",
			"        return max(array)");

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
				"class Solution:",
				"    def solve(num1, num2):",
				"        return num1 + num2");

		RunRequest request = new RunRequest();
		request.setCode(code);
		request.setLanguage(LANGUAGE);

		Problem problem = ProblemTestMethods.getSumProblem("\n2\n3");
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

		String[] inputs = {"p1", "2", "3.0", "4", "true", "[p6]", "[7]", "[8.0]", "[9]", "[false]"};

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
				OutputParser.DELIMITER_TEST_CASE,
				inputs[0], inputs[1], inputs[2], inputs[3], inputs[4],
				inputs[5], inputs[6], inputs[7], inputs[8], inputs[9],
				OutputParser.DELIMITER_SUCCESS,
				"0",
				"");

		assertTrue(runDto.isStatus());
		assertEquals(expected, runDto.getOutput());
	}
}

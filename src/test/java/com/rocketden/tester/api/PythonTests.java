package com.rocketden.tester.api;

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

	private static final String CODE = "print('hello')";
	private static final Language LANGUAGE = Language.PYTHON;

	@Test
	public void runRequestSuccess() throws Exception {
		RunRequest request = new RunRequest();
		request.setCode(CODE);
		request.setLanguage(LANGUAGE);
		request.setProblem(new Problem());

		MvcResult result = this.mockMvc.perform(post(POST_RUNNER)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(UtilityTestMethods.convertObjectToJsonString(request)))
				.andDo(print()).andExpect(status().isOk())
				.andReturn();

		String response = result.getResponse().getContentAsString();
		RunDto runDto = UtilityTestMethods.toObject(response, RunDto.class);

		assertTrue(runDto.isStatus());
		assertEquals("hi", runDto.getOutput());
	}
}

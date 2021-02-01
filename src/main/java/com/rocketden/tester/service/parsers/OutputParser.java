package com.rocketden.tester.service.parsers;

import com.rocketden.tester.dto.RunDto;
import org.springframework.stereotype.Service;

@Service
public class OutputParser {

    public static final String DELIMITER_TEST_CASE = "###########_TEST_CASE_############";
    public static final String DELIMITER_SUCCESS = "###########_SUCCESS_############";
    public static final String DELIMITER_FAILURE = "###########_FAILURE_############";

    public RunDto parseOutput() {
        // Requires discussion on how output will be stored and read in

        return null;
    }
}

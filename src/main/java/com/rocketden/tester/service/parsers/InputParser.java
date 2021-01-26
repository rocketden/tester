package com.rocketden.tester.service.parsers;

import com.rocketden.tester.model.problem.ProblemTestCase;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InputParser {

    // TODO: requires error validation not just for invalid format but e.g. "abc" not a valid char type
    public List<Object> parseTestCase(ProblemTestCase testCase) {
        return null;
    }
}

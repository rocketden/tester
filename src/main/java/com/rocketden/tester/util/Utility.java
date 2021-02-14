package com.rocketden.tester.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.rocketden.tester.model.problem.ProblemIOType;

public class Utility {

    // List of the array types.
    public static final List<ProblemIOType> arrayTypes = 
        Collections.unmodifiableList(Arrays.asList(ProblemIOType.ARRAY_BOOLEAN,
        ProblemIOType.ARRAY_CHARACTER, ProblemIOType.ARRAY_DOUBLE,
        ProblemIOType.ARRAY_INTEGER, ProblemIOType.ARRAY_STRING));

    // Private constructor to remove SonarLint warning.
    private Utility() {}
}

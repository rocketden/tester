package com.rocketden.tester.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultDto {
    private String console;
    private String userOutput;
    private String error;
    private String correctOutput;
    private boolean correct;
}



/**

There should be an object that has the console / output / error for each method.
For any result, either the output or the error exists while the other is null.
Maybe a <ResultDto>, with the above fields.
Perhaps this object also holds the correct solution, as well as booleans.
I could also include the PlayerCode object, though I'm uncertain if matters.
Long runtime as well.
I should use startTime, numCorrect, and numTestCases as well.

These are in a List that match

 */

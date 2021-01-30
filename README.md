# RocketDen Tester

This repository holds the source code for our application that takes in user code
and runs it against a set of test cases. 

## Manual Docker Setup and Usage

To manually set up, prepare, and test the Docker system used in this app, follow these steps:

* Run `docker-compose up` in the `docker` folder any time you make changes to files in that folder
  * This will build the Docker images that serve as "templates" for our disposable containers
* Run `start.sh` in the `docker` folder passing in the name of a directory in `temp` and language
  * This will create and run a disposable container that executes the code in the given folder
  * e.g. `bash start.sh folder123 java`

## General Structure

* User submits their code and the problem through a POST request
* A temporary folder in `temp` is created that stores the user code and generated driver/test program
* A docker container is created from this folder and executes the driver program
* The output is captured and returned to the user

## Request Structure

The input for each POST request should contain three things: the problem, user code, and language.

The problem provides several key pieces of information for judging: 

* General name of method to implement (dynamic based on language, e.g. `sortList` vs. `sort_list`)
* Names and types of method parameters (e.g. 1 param: "array", of type `ARRAY_INTEGER`)
* Return type of method (e.g. `STRING`)
* Test case inputs (format described below)
* Test case outputs (correct answers to each input)  

The boilerplate code for each problem is dynamically generated in Rocket Den's 
[main](https://github.com/rocketden/main) repository, with the method typically
residing in a `Solution` class or similar (depending on the language of choice). 
Users are then responsible for implementing the method according to the problem
description to pass the test cases.  

### Input Format

The test case inputs are created in a partial JSON format. If a problem has N
method parameters, then a corresponding test case will be N lines long, with
the ith line containing the JSON input for the ith method parameter. 

e.g. A problem with method parameters `String[] values` and `Integer num` could 
have the following test case input (note how each individual line is JSON-parsable): 

```
[hello, world]
5
```

## Driver Program

After receiving a valid request, the tester service will generate a temporary
folder where it will save the user's code in a `Solution.x` file (where `x` is
the file extension for the given language) and generate a `Driver.x` file. The
driver file contains the driver program that calls the user's solution code with
each test case, capturing and returning the output of their program. After being
run in an isolated docker container, the submission results are then returned 
from the POST request and the temporary folder deleted.

### Driver Generation

The driver program is dynamically generated based on the user's language of choice
and by the details of the problem. For example, for a problem `int findMax(int[] array)`
solved in Java, the driver program might be generated to look like the following:

```java
public class Driver {
    public static void main(String[] args) {
        int[] test_1_param_1 = {1, 2, 3};
        int[] test_2_param_1 = {1, 2, 3};
        ...
        
        try {
            System.out.println(DELIMITER_TEST_CASE);
            int output_1 = new Solution().findMax(test_1_param_1);

            System.out.println(DELIMITER_SUCCESS);
            captureOutput(output_1);
        } catch (Exception e) {
            System.out.println(DELIMITER_FAILURE);
            e.printStackTrace();
        }

        ...
    }
}
```

Above, `captureOutput`, and `captureError` is an additionally generated method 
that serializes and prints the user return object to the console. The resulting
output from the user program is then parsed, judged, and sent back to the user. 

## Output Format

The output of each driver program is used to judge the results of the user's
program. The output follows a template similar to the following: 

```
###########_TEST_CASE_############
test 1 2 3 (user console output)
here
###########_SUCCESS_############
3
###########_TEST_CASE_############
test 4 8 12
here
###########_SUCCESS_############
12
###########_TEST_CASE_############
test -2 0 5
###########_FAILURE_############
java.lang.ArithmeticException: / by zero

at Solution.runSuccess(Solution.java:25)

...

``` 

The delimiters are used to determine where one test case ends and another begins.
For each test case, the first few lines are whatever the user prints to the console
in their program. This is followed by either a success delimiter (the program runs
without any exceptions) or a failure delimiter (an error occurred and thus the user's
code was not able to return an answer). The lines afterwards are either the serialized
return output or the error message.  


### Output Serialization Formats 

When serialized into strings, the returned objects are directly compared to the 
given outputs for the problem. Therefore, both the serialization process and the
user-provided outputs should follow a specific JSON-type format in order to 
ensure proper judging. 

The following are the supported types for method parameters and return type for 
a problem, as well as the desired serialization format. Note that brackets, quotes,
and spaces must match perfectly in order for the answers to match properly. 

| Syntax                     | Example Serialization      |
| -------------------------- | -------------------------- |
| `String`                   | `"example"`                |
| `INTEGER`                  | `12`                       |
| `DOUBLE`                   | `5.405`                    |
| `CHARACTER`                | `"A"`                      |
| `BOOLEAN`                  | `true`                     |
| `ARRAY_STRING`             | `["ab", "cd"]`             |
| `ARRAY_INTEGER`            | `[-10,20]`                 |
| `ARRAY_DOUBLE`             | `[0.0,5.75,-1.12]`         |
| `ARRAY_CHARACTER`          | `["a", "b"]`               |
| `ARRAY_BOOLEAN`            | `[true,false]`             |


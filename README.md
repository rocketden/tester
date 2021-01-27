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
* Test case outputs (TBD whether provided manually or through a solution file) 

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
            captureConsole();
            int output_1 = new Solution().findMax(test_1_param_1);
            captureOutput(output_1);
        } catch (Exception e) {
            captureError(e);
        }

        ...
    }
}
```

Above, `captureConsole`, `captureOutput`, and `captureError` are additional 
generated methods that capture all 3 output types of the user's program, which
will be evaluated and returned later on (TBD: how output will be saved).


## Supported Types 

The following are the supported types for method parameters and return type for 
a problem: 

* `STRING`
* `INTEGER`
* `DOUBLE`
* `CHARACTER`
* `BOOLEAN`
* `ARRAY_STRING`
* `ARRAY_INTEGER`
* `ARRAY_DOUBLE`
* `ARRAY_CHARACTER`
* `ARRAY_BOOLEAN`


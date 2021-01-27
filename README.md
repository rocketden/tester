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

General description of how it works. 

* Description of boilerplate code 
* Description of input test case generation 
* Description of generating call to method
* Desription of capturing the output and saving it somehow (needs discussion)
* Potential mention of solution code
* Also mention error capturing and console print capturing


## Supported Types 

The following are the supported types for input and output of each method/problem: 

* STRING
* INTEGER
* DOUBLE
* CHARACTER
* BOOLEAN
* ARRAY_STRING
* ARRAY_INTEGER
* ARRAY_DOUBLE
* ARRAY_CHARACTER
* ARRAY_BOOLEAN


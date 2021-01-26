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

## Input

* Description of problem info (return, params)
* Description of user code (in a Solution class auto generated on main)
* Description of test cases (potentially JSON format, how it's parsed)
* Potentially a solution file also submitted along with it

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


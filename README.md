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

## Proposed Structure

* User submits their code
* A temporary folder in `temp` is created that stores the user code and driver files
* A docker container is created from this folder and executes the driver program
* The output is captured and returned to the user

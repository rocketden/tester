#!/bin/bash

# Run this command to build the Docker image used in the app
docker build --tag 'rocketden/tester' ../docker

# Remove dangling images that are no longer needed
docker rmi `docker images -f "dangling=true" -q`

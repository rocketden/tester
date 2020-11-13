#!/bin/bash

# Create and run a container from the given temp directory

if [ $# -eq 0 ]
  then
    echo "Please supply a directory within temp/ to mount this container with"
    exit 1
fi

docker run --rm -v "$PWD/../temp/$1:/app/code" -t rocketden/tester

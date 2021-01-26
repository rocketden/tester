#!/bin/bash

# Create and run a container from the given temp directory
if [ $# -eq 0 ]
  then
    echo "Please supply a directory within temp/ to mount this container with as the first argument."
    exit 1
fi

if [ $# -eq 1 ]
  then
    echo "Please supply a language (see docker-compose.yml) to run as the second argument."
    exit 1
fi

docker run --rm -v "$PWD/../temp/$1:/app/code" -t "docker_$2"

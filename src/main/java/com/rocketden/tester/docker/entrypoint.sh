#!/bin/bash

# Docker container entrypoint script
# Copies given folder contents from the temp directory into docker container and runs the driver script
# Example usage: ./entrypoint.sh folder123

#cp -a ../temp/$1/. .
bash ./code/run.sh

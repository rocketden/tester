# Copy folder in temp directory into docker container, then run it
# Example usage: ./setup.sh folder123

cp ../temp/$1 .
bash ./run.sh

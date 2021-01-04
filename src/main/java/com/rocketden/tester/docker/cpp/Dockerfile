# Get the base Ubuntu image from Docker Hub
FROM ubuntu:latest

# Update apps on the base image
RUN apt-get -y update && apt-get install -y

# Install the Clang compiler
RUN apt-get -y install clang

# Copy the current folder which contains C++ source code to the Docker image under /usr/src
COPY . /

# Specify the working directory
WORKDIR .

CMD pwd

# Use Clang to compile the Test.cpp source file
RUN clang++ -o Script script.cpp

# Run the output program from the previous step
CMD ["./script"]

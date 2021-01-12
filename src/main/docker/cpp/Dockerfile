# Get the base Ubuntu image from Docker Hub
FROM ubuntu:latest

# Update apps on the base image
RUN apt-get -y update && apt-get install -y

# Install the Clang compiler
RUN apt-get -y install clang

# Copy the current folder which contains C++ source code to the Docker image under /usr/src
COPY . . 
# Specify the working directory
WORKDIR /code

ADD start.sh /
RUN chmod +x /start.sh

CMD ["/start.sh"]

# Use a smaller Java runner
FROM frolvlad/alpine-java:jdk8-slim AS builder

# Copy the current folder which contains Java source code to the Docker image
COPY . .

# Specify working directory for created containers
WORKDIR /code

# Specify the bash executable to run
ENTRYPOINT ["sh", "/start.sh"]
CMD ["/start.sh"]

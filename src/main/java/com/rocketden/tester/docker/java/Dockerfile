# Use a smaller Java runner.
FROM frolvlad/alpine-java:jdk8-slim AS builder

# Copy all contents of current folder into the Docker image (the start.sh file).
COPY . .

# Set the working directory.
WORKDIR /code

# Specify what bash executable to run.
ENTRYPOINT ["sh", "/start.sh"]
CMD ["/start.sh"]

# Specify base image to build off of
FROM bash:4.4

# Specify working directory for created containers
WORKDIR /app

# Copy setup script into container
COPY setup.sh .

# Run setup script to copy relevant files and test user code
RUN bash setup.sh

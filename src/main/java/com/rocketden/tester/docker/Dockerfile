# Specify base image to build off of
FROM bash:4.4

# Specify working directory for created containers
WORKDIR /app

# Take in the folder name in temp that contains the user code
ARG folder

# Copy these files into the Docker container
COPY ../temp/$folder .

# Run the driver script
RUN bash run.sh


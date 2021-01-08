# Specify base image to build off of
FROM bash:4.4

# Specify working directory for created containers
WORKDIR /app

# Copy entrypoint script into container
COPY entrypoint.sh .

# Make entrypoint script executable
RUN chmod +x entrypoint.sh

# Run entrypoint script to call driver code
ENTRYPOINT ["bash", "entrypoint.sh"]

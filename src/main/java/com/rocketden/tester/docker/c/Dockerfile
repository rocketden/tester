FROM gcc:4.9

# Copy the current folder which contains C source code to the Docker image
COPY . .

# Specify working directory for created containers
WORKDIR /code

# Add the start script, make it executable, and run it
ADD start.sh /
RUN chmod +x /start.sh
CMD ["/start.sh"]

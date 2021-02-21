FROM rust:1.31
WORKDIR /code
COPY . .
# Specify what bash executable to run.
ADD start.sh /
RUN chmod +x /start.sh
CMD ["/start.sh"]

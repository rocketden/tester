FROM rust:1.31-alpine

WORKDIR /usr/src/myapp
COPY . .

RUN cargo install --path .

CMD ["script"]
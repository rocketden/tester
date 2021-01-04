FROM frolvlad/alpine-java:jdk8-slim AS builder

WORKDIR /app


COPY . .

CMD [ "javac", "./code/run.sh" ]
CMD [ "java", "./code/run.java" ]
FROM frolvlad/alpine-java:jdk8-slim AS builder

WORKDIR /app/code


COPY . .

CMD [ "javac", "./script.java" ]
CMD [ "java", "Script" ]
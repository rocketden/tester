FROM ruby:2.5-alpine

RUN bundle config --global frozen 1


WORKDIR /app

COPY . .

CMD [ "ruby", "./code/run.sh" ]
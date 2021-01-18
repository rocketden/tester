FROM ruby:2.5-alpine
RUN bundle config --global frozen 1
WORKDIR /code
COPY . .
CMD [ "ruby", "./script.rb" ]

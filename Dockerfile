FROM openjdk:8
RUN apt update
RUN apt install -y maven
WORKDIR /app
COPY . /app
ENTRYPOINT ["bash", "start.sh"]
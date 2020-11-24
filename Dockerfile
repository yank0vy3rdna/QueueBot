FROM openjdk:8
RUN apt update
RUN apt install -y maven
WORKDIR /app
COPY . /app
RUN mvn package
ENTRYPOINT ["mvn", "spring-boot:run"]
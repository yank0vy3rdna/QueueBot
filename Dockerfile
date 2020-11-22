FROM openjdk:8
WORKDIR /app
COPY build/queue_bot-0.0.1.jar /app
CMD ["java", "-jar", "queue_bot-0.0.1.jar"]
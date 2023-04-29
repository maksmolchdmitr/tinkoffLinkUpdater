FROM openjdk:17
ARG bot_token
COPY target/bot-1.0-SNAPSHOT.jar app.jar
ENV BOT_TOKEN=$bot_token
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app.jar"]
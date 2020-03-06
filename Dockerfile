FROM openjdk:11
ARG JAR_FILE
COPY ${JAR_FILE} /app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=${PROFILE}", "-Dspring.cloud.config.uri=${CONFIG_URI}","-jar","/app.jar"]

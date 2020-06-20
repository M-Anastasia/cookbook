FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} cookbook-1.jar
ENTRYPOINT ["java","-jar","/cookbook-1.jar"]
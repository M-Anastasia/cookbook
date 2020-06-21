FROM openjdk:8-jdk-alpine
RUN mkdir /opt/uploads
ARG recipes_images_path='/opt/uploads/'
ENV RECIPES_IMAGES=$recipes_images_path
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} cookbook-1.jar
ENTRYPOINT ["java","-jar","/cookbook-1.jar"]
FROM openjdk:8-jdk-alpine
RUN mkdir /opt/uploads
ARG recipes_images_path='/opt/uploads/'
ENV RECIPES_IMAGES=$recipes_images_path
ADD cookbook-1.jar cookbook-1.jar
ENTRYPOINT ["java","-jar","/cookbook-1.jar"]
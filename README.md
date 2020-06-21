# cookbook
This is a pet-project for Spring learning.

This is a service where you can create your own recipe book. You can post recipes for all users or just for yourself, and you can also share hidden recipes with your friends.

# Preview
// todo

# Technologies
* Spring Boot
* Spring Security
* Spring Data
* DB - PostgreSQL
* Thymeleaf templates

Also i used templates from https://www.free-css.com/free-css-templates
# How to launch
To launch this project you need to have jdk 8, maven, PostgreSQL on your machine.
1) run cookbook/src/main/resources/db/db_init.sql to create data base stucture for this application;
2) run project with maven: 
```sh
mvn spring-boot:run
```

# Launch with Docker
To launch this project with Docker, you need to have git and Docker installed
1) Clone this project to your machine
```sh
git clone https://github.com/M-Anastasia/cookbook.git
```
2) Go to cookbook/resources/db/
3) Start database in docker
```sh
docker build -t cookbook-postgres-image
docker run --name cookbook-postgres-container -p 5432:5432 cookbook-postgres-image
```
4) Build jar with maven
```sh
mvn clean package
```
5) Go to root directory of cookbook app and run this application in docker
```sh
docker build -t cookbook-image
docker run --name cookbook-container -v ~/uploads:/opt/uploads -p 8080:8080 cookbook-image
```
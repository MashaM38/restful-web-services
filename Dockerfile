FROM maven:3.6.3

WORKDIR /app

COPY . /app

RUN mvn clean package

FROM adoptopenjdk/openjdk8:ubi

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} application.jar

ENTRYPOINT ["java","-jar","application.jar"]
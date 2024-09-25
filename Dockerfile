FROM maven:3.9.8-eclipse-temurin-17-alpine

WORKDIR /home/app

COPY . /home/app

RUN mvn clean install -P prod -Dmaven.test.skip=true

EXPOSE 8080

CMD ["java", "-jar", "./target/techchallenge-1.0.0-SNAPSHOT.jar"]
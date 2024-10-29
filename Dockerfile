FROM maven:3.8.4-openjdk-17

COPY pom.xml .
COPY src ./src

RUN mvn clean install

EXPOSE 8090

CMD ["java", "-jar", "./target/wex-0.0.1-SNAPSHOT.jar"]
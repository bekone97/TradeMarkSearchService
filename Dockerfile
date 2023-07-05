FROM maven:3.8.3-openjdk-11 AS build
WORKDIR /workspace/app
COPY . /workspace/app/.
RUN mvn -f /workspace/app/pom.xml clean package -DskipTests

FROM openjdk:11-jdk
COPY --from=build /workspace/app/target/trademarksearchservice-0.0.1-SNAPSHOT.jar /trademarksearchservice.jar
COPY --from=build /workspace/app/infastructure/init /init
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/trademarksearchservice.jar"]
FROM maven:3.9.8-eclipse-temurin-22-alpine AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:22.0.2_9-jre AS production
WORKDIR /app
COPY --from=build /app/target/*.jar MovieManager.jar
EXPOSE 8080
RUN groupadd spring && \
    useradd -g spring appuser
USER appuser
ENTRYPOINT ["java", "-jar", "MovieManager.jar"]
FROM maven:3.9.9-eclipse-temurin-23-alpine AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:23_37-jre-alpine AS production
WORKDIR /app
COPY --from=build /app/target/*.jar MovieManager.jar
EXPOSE 8080
RUN addgroup -S spring && \
    adduser -S kuba -G spring
USER kuba
ENTRYPOINT ["java", "-jar", "MovieManager.jar"]
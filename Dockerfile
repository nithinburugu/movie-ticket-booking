<<<<<<< HEAD
# Build stage
FROM maven:3.8.3-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Package stage
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/algobharat-0.0.1-SNAPSHOT.jar app.jar
RUN mkdir -p /app/data
VOLUME /app/data
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=${SPRING_PROFILES_ACTIVE}"]
=======
# Build stage
FROM maven:3.8.3-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Package stage
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/algobharat-0.0.1-SNAPSHOT.jar app.jar
RUN mkdir -p /app/data
VOLUME /app/data
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=${SPRING_PROFILES_ACTIVE}"]
>>>>>>> 794349a5c0d980e089669a35d950f45fbd72b4c9

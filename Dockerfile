# Build
FROM eclipse-temurin:25-jdk-alpine AS build
RUN apk add --no-cache maven
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Final image
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

# Copiar el jar
COPY --from=build /app/target/*.jar app.jar



# Variables para Oracle
ENV TNS_ADMIN=/app/wallet
ENV JAVA_TOOL_OPTIONS="-Doracle.net.tns_admin=/app/wallet"

EXPOSE 8081
ENTRYPOINT ["java","-jar","app.jar"]

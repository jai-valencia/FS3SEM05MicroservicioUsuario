###Etapa de definición de build
##FROM eclipse-temurin:17-jdk-alpine as build
##RUN apk add --no-cache maven
##WORKDIR /app
##COPY pom.xml .
##COPY src ./src
##RUN mvn clean package -DskipTests
##
###Etapa de definición de la imagen final
##FROM eclipse-temurin:17-jre-alpine
##WORKDIR /app
##COPY --from=build /app/target/*.jar app.jar
##EXPOSE 8081
##ENTRYPOINT ["java","-jar","app.jar"]

# ================================
# ETAPA 1: Build con Maven + JDK
# ================================
FROM eclipse-temurin:17-jdk AS build

# Instalar Maven
RUN apt-get update && apt-get install -y maven

WORKDIR /app

COPY pom.xml .
COPY src ./src

# Compilar el proyecto
RUN mvn clean package -DskipTests


# ================================
# ETAPA 2: Imagen final (JRE)
# ================================
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copiar JAR compilado
COPY --from=build /app/target/*.jar app.jar

# Copiar Oracle Wallet
COPY wallet/ /app/wallet

# Configurar el wallet
ENV TNS_ADMIN=/app/wallet

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

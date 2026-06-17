# Etapa 1: Construcción (Build) usando una imagen oficial de Maven con Java 21
FROM maven:3.9.5-eclipse-temurin-21 AS builder
WORKDIR /app

# Copiamos el pom.xml y el código fuente al contenedor
COPY pom.xml .
COPY src ./src

# Compilamos el proyecto dentro de Docker (ignoramos los tests para que sea más rápido)
RUN mvn clean package -DskipTests

# Etapa 2: Ejecución (Run) usando una imagen más ligera solo con Java 21
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /opt/app

# Copiamos el archivo .jar generado en la Etapa 1
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
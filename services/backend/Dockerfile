# syntax=docker/dockerfile:1.7

# ====== BUILD ======
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace

# Baixa dependências e plugins em cache (sem compilar)
COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -q -DskipTests dependency:go-offline

# Copia o código-fonte e faz o build ignorando os testes
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -DskipTests clean package

# ====== RUNTIME ======
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Boas práticas para Java em containers
ENV JAVA_OPTS="-XX:+UseG1GC -XX:MaxRAMPercentage=75 -XX:+HeapDumpOnOutOfMemoryError -Djava.security.egd=file:/dev/./urandom"
COPY --from=build /workspace/target/*.jar /app/app.jar
EXPOSE 8080

ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]
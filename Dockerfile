FROM maven:3.9.0-openjdk-17 AS build

WORKDIR /app

# Copiar pom.xml primeiro para cache de dependências
COPY pom.xml .

# Baixar dependências
RUN mvn dependency:go-offline

# Copiar código fonte
COPY src ./src

# Construir aplicação (sem testes para ser mais rápido)
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim

WORKDIR /app

# Porta que o Render espera
EXPOSE 10000

# Copiar o JAR construído
COPY --from=build /app/target/todolist-0.0.1-SNAPSHOT.jar app.jar

# Importante: Usar a variável PORT do Render
ENTRYPOINT ["java", "-Dserver.port=${PORT:-10000}", "-jar", "app.jar"]
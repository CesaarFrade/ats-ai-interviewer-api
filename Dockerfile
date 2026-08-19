# Construcción (Build)
FROM gradle:8-jdk17-alpine AS build
WORKDIR /app
# Copiamos los archivos de configuración de Gradle
COPY build.gradle settings.gradle ./
# Copiamos el código fuente completo
COPY src ./src
# Compilamos el proyecto y generamos el .jar saltando los tests
RUN gradle bootJar -x test --no-daemon

# Ejecución (Run)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar
# Exponemos el puerto
EXPOSE 8080
# Comando para arrancar la app
ENTRYPOINT ["java", "-jar", "app.jar"]
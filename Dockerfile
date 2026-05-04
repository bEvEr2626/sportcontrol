FROM eclipse-temurin:21-jdk AS build
WORKDIR /workspace

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN ./mvnw -q -DskipTests dependency:go-offline

COPY src/ src/
RUN ./mvnw -DskipTests package

FROM eclipse-temurin:21-jre
WORKDIR /app

RUN useradd -r -u 1001 -g root appuser

COPY --from=build /workspace/target/sportcontrol-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8080

ENV JAVA_OPTS=""
USER appuser

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar --server.port=$PORT"]

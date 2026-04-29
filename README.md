# SportControl API

SportControl is a Spring Boot REST API for managing sports events, locations, teams, and tournaments. The service follows a layered architecture (controller, service, repository) and exposes a DTO-based API.

## Features
- CRUD operations for core domain objects
- Location-based search
- Caching with Ehcache and JCache
- Async match processing demo settings

## Tech Stack
- Java 21, Spring Boot 4.0
- Spring Web, Spring Data JPA, Bean Validation
- PostgreSQL (runtime), H2 (tests)
- MapStruct, Lombok
- Springdoc OpenAPI
- Maven, JaCoCo, Checkstyle

## Requirements
- JDK 21
- Maven 3.9+ or the included Maven wrapper
- PostgreSQL

## Configuration
Default configuration is in src/main/resources/application.properties:

```
spring.datasource.url=jdbc:postgresql://localhost:5432/sport_db
spring.datasource.username=sport_user
spring.datasource.password=${DB_PASSWORD}
```

Set the `DB_PASSWORD` environment variable before running. You can also override settings with Spring standard environment variables such as `SPRING_DATASOURCE_URL` and `SPRING_DATASOURCE_USERNAME`.

Async demo settings:

```
app.async.match-demo-delay-ms=30000
app.async.match.core-pool-size=16
app.async.match.max-pool-size=64
app.async.match.queue-capacity=1000
```

## Run locally
1. Create the PostgreSQL database and user.
2. Export `DB_PASSWORD` for the configured database user.
3. Start the application.

```
./mvnw spring-boot:run
```

Build and run the jar instead:

```
./mvnw clean package
java -jar target/sportcontrol-0.0.1-SNAPSHOT.jar
```

## API docs
- http://localhost:8080/swagger-ui/index.html
- http://localhost:8080/v3/api-docs

## Testing and quality
- `./mvnw test`
- `./mvnw verify` (runs Checkstyle and JaCoCo)
- Coverage threshold: 85% line coverage for key packages
- SonarCloud: https://sonarcloud.io/summary/overall?id=bEvEr2626_sportcontrol&branch=main

## Load testing
JMeter plans and reports are stored in the jmeter/ directory:

```
jmeter/async-all-load.jmx
jmeter/async-load-test-report.md
```

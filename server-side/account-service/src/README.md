# account-service

## Description
The **account-service** is a backend microservice responsible for managing customer accounts. It handles account creation, retrieval, updates, and ensures secure management of sensitive account information.

## Installation and Running
This service is intended to be run locally as part of the project under the `account-service` directory inside the `server-side` folder.

The service listens on port **8081**.
## API Documentation
API documentation is available via Swagger UI at: http://localhost:8081/swagger-ui.html

## Dependencies
- Spring Boot Starter Web
- Spring Data MongoDB
- Caffeine Cache
- Lombok
- Swagger/OpenAPI for API documentation

## Testing
Automated tests are included in the service. Run tests using:
```bash
cd account-service
mvn test

### Run locally
```bash
cd account-service
./mvnw spring-boot:run

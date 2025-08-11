# transaction-service

## Overview
`transaction-service` manages financial transactions: transfers, deposits, and withdrawals.  
It communicates with `account-service` (port 8081) to verify and update account balances, ensuring transactional integrity with rollback support.

## Features
- Transfer, deposit, withdrawal handling
- Transaction lifecycle management (PENDING, COMPLETED, FAILED)
- Rollback on failure

## Installation and Running
- This service is intended to be run locally as part of the project under the `transaction-service` directory inside the `server-side` folder.
- Requires `account-service` running on port 8081

The service listens on port **8082**.
## API Documentation
API documentation is available via Swagger UI at: http://localhost:8082/swagger-ui.html

## Testing
Automated tests are included in the service. Run tests using:
```bash
cd transaction-service
mvn test

### Run locally
```bash
cd transaction-service
./mvnw spring-boot:run

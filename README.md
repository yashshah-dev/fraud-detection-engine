# Fraud Detection Engine

A Java-based, production-ready fraud detection engine built with Spring Boot. It provides dynamic rule management, robust validation, OpenTelemetry tracing, access logging, and REST APIs for transaction risk scoring and rule management. Rules, transactions, and alerts are persisted in PostgreSQL, and tests run against an in-memory H2 database.

## Project Structure

This project follows the standard Maven project structure:
```
src/main/java/        # Main source code
src/test/java/        # Test source code
target/               # Compiled classes and built artifacts
src/main/resources/   # Main configuration (application.properties)
src/test/resources/   # Test configuration (application.properties for H2)
```

## Features

- **Spring Boot** backend with RESTful APIs
- **Dynamic rule engine** using SpEL (Spring Expression Language)
- **Rule management**: deploy, activate, deactivate, update, list, delete (all persisted in PostgreSQL)
- **Transaction risk scoring** endpoint
- **Alerting** for flagged transactions
- **Robust input validation** (Bean Validation, custom annotations for card/IP)
- **OpenTelemetry** tracing and access logging
- **Global exception handling**
- **CORS** configuration for frontend integration
- **JUnit tests** for all rule management and transaction endpoints
- **H2 in-memory database** for isolated test runs

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- PostgreSQL running on `localhost:5432` (see `src/main/resources/application.properties` for DB config)

## Configuration

- **Production DB:** Edit `src/main/resources/application.properties` for your PostgreSQL credentials.
- **Test DB:** `src/test/resources/application.properties` is preconfigured for H2 in-memory database (no setup needed).

## Building the Project

To build the project, run:

```bash
mvn clean install
```

## Running Tests

To run the tests (uses H2, does not touch PostgreSQL):

```bash
mvn test
```

## Running the Application

To start the backend (ensure PostgreSQL is running and configured):

```bash
mvn spring-boot:run
```

## API Endpoints

- `POST /api/rules` — Add or update a rule
- `POST /api/rules/activate/{id}` — Activate a rule
- `POST /api/rules/activateAll` — Activate all rules
- `POST /api/rules/deactivate/{id}` — Deactivate a rule
- `GET /api/rules/all` — List all rules
- `GET /api/rules/active` — List active rules
- `DELETE /api/rules/delete/{id}` — Delete an inactive rule
- `POST /api/transactions/score` — Score a transaction for fraud risk

## Development Notes

- Main application code: `src/main/java/com/frauddetection`
- Tests: `src/test/java/com/frauddetection`
- Logging output: `logs/fraud-detection-engine.log`
- All rule management is now database-backed (no in-memory registry)

## Git & Version Control

- `.gitignore` is set up for Maven, logs, IDE, and environment files.
- Project is ready for GitHub remote and CI/CD integration.

---

For more details, see the code and comments in each module. Contributions and issues are welcome!

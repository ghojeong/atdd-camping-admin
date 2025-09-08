# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview
This is a legacy camping administration system built for ATDD (Acceptance Test-Driven Development) learning. The system intentionally contains complex structures and duplicated code to simulate real-world legacy systems. It manages camping reservations, products, campsites, rentals, and revenue tracking.

## Build and Development Commands

### Basic Operations
```bash
# Build the project
./gradlew build

# Run the application (port 8080)
./gradlew bootRun

# Run tests
./gradlew test

# Run specific test class
./gradlew test --tests "ClassName"

# Run Cucumber tests specifically
TEST_BASE_URL=http://localhost:8080 ./gradlew test

# Clean build
./gradlew clean build
```

### Database Access
- H2 Console: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`, Password: (empty)

## Architecture Overview

### Domain Structure
The system follows a typical Spring Boot layered architecture with some legacy anti-patterns:

- **Entities**: `com.camping.admin.domain.entity` - JPA entities for core business objects
  - `Reservation` - Camping reservations with status tracking
  - `Campsite` - Physical camping sites 
  - `Product` - Rentable/sellable items (RENTAL/SALE types)
  - `RentalRecord` - Product rental tracking
  - `SalesRecord` - Product sales tracking
  - `Customer` - Customer information

- **Controllers**: Two controller packages
  - `com.camping.admin.controller` - REST API controllers for admin operations
  - `com.camping.admin.web` - Web/console controllers for UI rendering

- **Repositories**: Direct JPA repository pattern without service layers (intentional legacy pattern)

### Key Endpoints
Authentication:
- `POST /auth/login` - JWT token generation (admin/admin123)

Admin API endpoints:
- `GET /admin/reservations` - List all reservations
- `PATCH /admin/reservations/{id}/status` - Update reservation status
- `POST /admin/products` - Create new product
- `POST /admin/campsites` - Create new campsite  
- `POST /admin/rentals` - Create rental record

### Testing Strategy
This codebase is designed for implementing Cucumber-based acceptance tests:

- **Test Dependencies**: Already includes Cucumber 7.14.0, RestAssured 5.3.2, JUnit Platform Suite
- **Test Structure**: Tests should be created in `src/test/java/com/camping/admin/steps` for step definitions
- **Feature Files**: Gherkin scenarios go in `src/test/resources/features/`
- **Test Runner**: Use `@Suite` and `@SelectClasspathResource("features")` pattern

### Authentication
- JWT-based authentication with Bearer tokens
- Admin credentials: username="admin", password="admin123"  
- Cookie-based token storage (`AUTH_TOKEN` cookie)
- Token expiration: 30 minutes

### Database Initialization
The system uses `src/main/resources/data.sql` for test data initialization, including:
- 2 campsites (A-01, A-02)
- 12 products (mix of RENTAL and SALE types)
- 12 sample reservations with various dates
- Sales and rental records

### Legacy Code Characteristics
The controllers intentionally contain:
- Verbose null checking and error handling
- Direct repository access without service layers
- Map-based request handling instead of DTOs
- Redundant validation logic
- Mixed concerns in single methods

This structure is designed to be refactored as part of the ATDD learning process while maintaining test coverage.

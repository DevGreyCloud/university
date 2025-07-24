# University Management System

This project is a Spring Boot-based University Management System. It provides RESTful APIs for managing students, lecturers, and their relationships, with features such as rate limiting, error handling, and database migrations.

## Features
- Student and Lecturer CRUD operations with MySQL database for storage
- Relationship management between students and lecturers
- Rate limiting for API endpoints using Bucket4j
- Centralized error handling
- Database migrations using Flyway
- Performance and resiliency improvement via Caching using Caffeine
- Data validation with SQL constraints and using Jakarta validation
- Integration tests using H2

## Technologies Used
- Java 17+
- Spring Boot
- Maven
- MySQL
- Bucket4J (for rate limiting)
- Caffeine (for caching)
- Flyway (for database migrations)
- H2 (used for integration tests; can be configured for other DBs)

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven

#### Environment Variables
A `.env` file is expected at the root level of the project. It should contain the MySQL username and password:

```env
DB_USERNAME=your_mysql_username
DB_PASSWORD=your_mysql_password
```

This project expects `flyway.user` and `flyway.password` to be defined in your Maven `settings.xml` under a profile named `university`.

### Running the Application
1. Clone the repository:
   ```bash
   git clone <repo-url>
   cd university
   ```
2. Build and run the application:
   ```bash
   ./mvnw spring-boot:run
   ```
   Or, to build:
   ```bash
   ./mvnw clean install
   ```

### API Documentation (Swagger UI)
The interactive API documentation is available via Swagger UI:
- [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

Use Swagger UI to explore and test the available API endpoints.

### API Endpoints
- `/api/students` - Manage students
- `/api/lecturers` - Manage lecturers

See controller classes for detailed endpoint documentation.

### Database Migrations
Flyway migrations are located in `src/main/resources/db/migration/`.

## Testing
Run tests with:
```bash
./mvnw test
```

## Configuration
Application configuration is in `src/main/resources/application.yaml`.

## License
This project is licensed under the MIT License.

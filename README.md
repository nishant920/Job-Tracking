# JobTrack

JobTrack is a Spring Boot REST API for recording and managing job applications. Users can register, verify their email address, log in to receive a JWT, and maintain a private collection of applications.

## Features

- User registration and email verification
- Stateless JWT authentication with BCrypt password hashing
- Input validation on requests (e.g. email formats, required fields)
- Centralized global exception handling with standardized JSON error payloads
- Create, search, update, and delete job applications
- Track companies, salaries, skills, platforms, and application dates
- PostgreSQL persistence with Spring Data JPA
- HTML verification emails rendered with Thymeleaf

## Tech stack

- Java 17
- Spring Boot 4.0.5
- Spring Web MVC and Spring Security
- Spring Data JPA / Hibernate
- PostgreSQL
- JJWT
- Spring Mail and Thymeleaf
- Lombok
- Maven Wrapper

## Prerequisites

- JDK 17
- PostgreSQL
- SMTP credentials for verification emails

Maven does not need to be installed separately because the project includes the Maven Wrapper.

## Setup

### 1. Create the database

```sql
CREATE DATABASE "job-track";
```

The default configuration connects using environment variables for secrets:

```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
jwt.secret.password=${JWT_SECRET}
spring.mail.username=${MAIL_USERNAME}
spring.mail.password=${MAIL_PASSWORD}
app.base.url=${APP_BASE_URL}
```

Ensure these are set in your environment variables before running the application.

### 2. Configure email

Set the credentials in PowerShell:

```powershell
$env:MAIL_USERNAME="your-email@example.com"
$env:MAIL_PASSWORD="your-app-password"
```

Use an app password or provider-specific SMTP credential.

### 3. Run the API

On Windows:

```powershell
cd track-site
.\mvnw.cmd spring-boot:run
```

On macOS or Linux:

```bash
cd track-site
./mvnw spring-boot:run
```

The API starts at `http://localhost:7070`.

## Authentication flow

1. Register a user.
2. Open the verification link sent by email. It expires after 30 minutes.
3. Log in to receive a JWT.
4. Include the JWT with every job request:

```http
Authorization: Bearer <your-jwt>
```

All `/api/v1/job/**` endpoints require authentication.

## API reference

### Register

```http
POST /api/v1/user/save
Content-Type: application/json
```

```json
{
  "name": "Nisha",
  "email": "nisha@example.com",
  "password": "your-password"
}
```

Response is a sanitized user object without the password hash (`UserResponseDto`):
```json
{
  "id": 1,
  "name": "Nisha",
  "email": "nisha@example.com",
  "verified": false,
  "role": "AppUser"
}
```

### Verify email

```http
GET /api/v1/user/verify?token=<verification-token>
```

This route is normally opened through the verification email.

### Log in

```http
POST /api/v1/user/login
Content-Type: application/json
```

```json
{
  "email": "nisha@example.com",
  "password": "your-password"
}
```

The response body contains the JWT token.

### Create a job application

```http
POST /api/v1/job/save
Authorization: Bearer <your-jwt>
Content-Type: application/json
```

```json
{
  "profile": "Backend Developer",
  "company": {
    "name": "Example Corp",
    "location": "Bengaluru"
  },
  "salary": 1200000,
  "status": "APPLIED",
  "experience": 2,
  "skillSet": ["Java", "Spring Boot", "PostgreSQL"],
  "platform": "LinkedIn",
  "appliedDate": "2026-07-02"
}
```

Supported statuses: `APPLIED`, `REJECTED`, `INTERVIEWED`, `NO_RESPONSE`, and `OFFER`.

### Search by profile

```http
GET /api/v1/job/search?profile=Backend%20Developer
Authorization: Bearer <your-jwt>
```

Only applications belonging to the authenticated user are returned.

### Update status

```http
PUT /api/v1/job/{id}/status
Authorization: Bearer <your-jwt>
Content-Type: application/json
```

```json
{
  "status": "INTERVIEWED"
}
```

### Delete job application

```http
DELETE /api/v1/job/delete/{id}
Authorization: Bearer <your-jwt>
```

## Error Handling & Validation

Centralized error handling wraps all validation errors, authentication failures, and bad request parameters in a standard JSON format:

```json
{
  "error": "Validation Failed",
  "message": "email: Please provide a valid email address",
  "timestamp": "2026-07-21T01:30:00"
}
```

## Endpoint Testing (IDE Friendly)

The project includes an [api-tests.http](file:///c:/Users/nisha/OneDrive/Desktop/jobtrack-project/track-site/api-tests.http) file in the root of the `track-site` directory.
- Open this file in IntelliJ Ultimate (built-in HTTP Client) or install the free **RestfulTool** plugin in IntelliJ Community to run requests directly from the IDE window without needing Postman.

## Tests

Run tests from the `track-site` directory:

```powershell
.\mvnw.cmd test
```

Use `./mvnw test` on macOS or Linux.

## Configuration notes

- Hibernate uses `spring.jpa.hibernate.ddl-auto=update` in development.
- The server port is `7070`.
- `jwt.expiration.time` controls token lifetime.
- `app.base.url` is used to build verification links.

## Current limitations

- Automated test coverage is currently minimal.

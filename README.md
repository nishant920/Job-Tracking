# JobTrack

JobTrack is a Spring Boot REST API for recording and managing job applications. Users can register, verify their email address, log in to receive a JWT, and maintain a private collection of applications.

## Features

- User registration and email verification
- Stateless JWT authentication
- Create, search, and update job applications
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

The default development configuration connects with:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/job-track
spring.datasource.username=postgres
spring.datasource.password=postgres
```

Change these values in `track-site/src/main/resources/application.properties` if your setup differs.

### 2. Configure email

Set the credentials in PowerShell:

```powershell
$env:MAIL_USERNAME="your-email@example.com"
$env:MAIL_PASSWORD="your-app-password"
```

Add the SMTP settings required by your provider to `application.properties`. A typical Gmail configuration is:

```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
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

### Verify email

```http
GET /api/v1/user/verify?token=<verification-token>
```

This route is normally opened through the verification email.

### Log in

The current implementation accepts the login body on a `GET` request:

```http
GET /api/v1/user/login
Content-Type: application/json
```

```json
{
  "email": "nisha@example.com",
  "password": "your-password"
}
```

The response body contains the JWT.

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

## Tests

Run tests from the `track-site` directory:

```powershell
.\mvnw.cmd test
```

Use `./mvnw test` on macOS or Linux.

## Configuration notes

- Hibernate uses `spring.jpa.hibernate.ddl-auto=update`.
- The server port is `7070`.
- `jwt.expiration.time` controls token lifetime.
- `app.base.url` is used to build verification links.
- Move the database password and JWT secret out of `application.properties` before deployment.

## Current limitations

- Passwords are currently compared as plain text; add password hashing before production use.
- Login uses `GET` with a request body; `POST` is better supported by HTTP clients.
- A delete method exists in the controller but is not mapped to an HTTP route.
- Automated test coverage is currently minimal.

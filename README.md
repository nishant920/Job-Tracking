Track-site is a professional-grade backend application built with Spring Boot and Java 17. It serves as a centralized
  platform for users to track and manage their job applications across multiple job boards and companies.

  1. Core Functionalities
   * User Lifecycle Management:
       * Secure Registration: Handles user signup with password encryption.
       * Email Verification: Sends automated verification emails using Thymeleaf templates and Spring Mail.
       * JWT-Based Authentication: Implements stateless security using JSON Web Tokens (JWT) for secure API access.
   * Job Tracking System:
       * Application Logging: Record details such as Company Name, Job Profile, Salary, Required Experience, and Skill
         Sets.
       * Platform Tracking: Keep track of where you applied (e.g., LinkedIn, Indeed, Naukri).
       * Status Management: Update the lifecycle of an application from Applied to Interview or Rejected.

  2. Technical Architecture
  The project follows a clean, layered Spring Boot architecture:
   * Controllers: REST API endpoints (e.g., UserController, JobController) that handle HTTP requests.
   * Services: Business logic layer (e.g., UserService, JobService) where the core application rules reside.
   * Repositories: Data Access Layer using Spring Data JPA for seamless interaction with the PostgreSQL database.
   * Models/Entities: JPA-mapped classes (User, Job, Company) representing the database schema.
   * DTOs (Data Transfer Objects): Decouples the internal database models from the external API responses for better
     security and flexibility.
   * Security Filters: Custom AuthFilter to intercept and validate JWT tokens on every request.

  3. Technology Stack
  ┌─────────────────┬────────────────────────────────┐
  │ Category        │ Technology                     │
  ├─────────────────┼────────────────────────────────┤
  │ Language        │ Java 17                        │
  │ Framework       │ Spring Boot 4.0.5              │
  │ Security        │ Spring Security, JJWT          │
  │ Database        │ PostgreSQL                     │
  │ ORM             │ Spring Data JPA / Hibernate    │
  │ Email/Templates │ Spring Mail, Thymeleaf         │
  │ Build Tool      │ Maven                          │
  │ Utilities       │ Lombok (Boilerplate reduction) │
  └─────────────────┴────────────────────────────────┘

  4. Database Schema Highlights
   * Users Table: Stores user credentials, verification status, and roles.
   * Jobs Table: Stores job-specific data, linked to a specific user via a Foreign Key (user_id). It also uses
     @ElementCollection to store lists of required skills for each job.

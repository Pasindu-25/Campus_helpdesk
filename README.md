# Campus Help Desk

A web-based university help desk application built with **Java 21**, **Spring Boot**, **Spring MVC**, **Thymeleaf**, **Spring Data JPA**, and **Microsoft SQL Server**. It helps students submit and track support tickets while staff and administrators manage tickets and campus support operations.

## Contents

- [Features](#features)
- [Technology Stack](#technology-stack)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Database Configuration](#database-configuration)
- [Email Configuration](#email-configuration)
- [Run the Application](#run-the-application)
- [Build and Test](#build-and-test)
- [User Roles](#user-roles)
- [Project Structure](#project-structure)
- [Troubleshooting](#troubleshooting)
- [Security Notes](#security-notes)
- [Contributing](#contributing)

## Features

The current source code includes the following functionality:

- **Student portal**
  - Submit support tickets and view personal tickets.
  - Edit or delete tickets and escalate issues.
  - Attach documents to tickets.
  - View ticket-related pages and appointment functionality.
- **Staff portal**
  - View tickets assigned to a faculty or permitted scope.
  - Track open, in-progress, resolved, and critical tickets.
  - Update ticket status, reply to tickets, and handle spam.
- **Administration**
  - Admin and super-admin dashboards.
  - Manage users and ticket categories.
  - Manage faculties.
  - View ticket statistics and reports.
  - Reply to tickets.
- **Notifications**
  - Email notification support using Spring Boot Mail.
- **Persistence**
  - SQL Server database integration through Spring Data JPA and Hibernate.
- **Server-rendered web interface**
  - HTML pages rendered using Thymeleaf.

> Feature availability depends on the current implementation and configuration. This README does not imply that external email delivery or every workflow has been tested in every environment.

## Technology Stack

- Java 21
- Spring Boot 3.3.0
- Spring Web / Spring MVC
- Spring Data JPA and Hibernate
- Thymeleaf
- Microsoft SQL Server
- Microsoft SQL Server JDBC driver
- Spring Boot Mail (SMTP)
- Maven (Maven Wrapper included)
- Spring Boot Test / JUnit

## Prerequisites

Install or prepare the following:

1. **JDK 21** — install a Java Development Kit, not only a JRE.
2. **Microsoft SQL Server** — a local instance or a reachable server.
3. **SQL Server Management Studio (SSMS)** or another SQL client, recommended for creating and checking the database.
4. **Git** — to clone the repository.
5. An SMTP account only if you want email notifications to work.

Verify Java is available in your terminal:

```bash
java -version
```

The output should report Java 21. Verify the Maven Wrapper files are present in the project root (`mvnw`, `mvnw.cmd`, and `.mvn/wrapper`).

## Getting Started

### 1. Clone the repository

Replace `YOUR-USERNAME` and `YOUR-REPOSITORY` with your GitHub details:

```bash
git clone https://github.com/YOUR-USERNAME/YOUR-REPOSITORY.git
cd YOUR-REPOSITORY
```

If you downloaded the project as a ZIP instead, extract it and open a terminal in the directory containing `pom.xml`.

### 2. Configure SQL Server

Start SQL Server and ensure the instance accepts connections. Create the database:

```sql
CREATE DATABASE campus_helpdesk_db;
```

The application is currently configured for a SQL Server instance at `localhost:1433`. If your SQL Server uses a different host, port, instance, authentication method, or database name, update the datasource settings accordingly.

### 3. Configure application settings

Open:

```text
src/main/resources/application.properties
```

Use environment variables for credentials rather than committing passwords. A recommended configuration is:

```properties
spring.application.name=helpdesk

spring.datasource.url=${DB_URL:jdbc:sqlserver://localhost:1433;databaseName=campus_helpdesk_db;encrypt=true;trustServerCertificate=true}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false

spring.mail.host=${MAIL_HOST:smtp.gmail.com}
spring.mail.port=${MAIL_PORT:587}
spring.mail.username=${MAIL_USERNAME}
spring.mail.password=${MAIL_PASSWORD}
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

Set `DB_USERNAME` and `DB_PASSWORD` to credentials for your SQL Server. The example URL enables encryption and trusts the server certificate, which is often used for local development; configure certificate validation appropriately for production.

`spring.jpa.hibernate.ddl-auto=update` allows Hibernate to update the schema during development. For production, use a controlled database migration strategy and review schema changes before deployment.

### 4. Set environment variables

**Windows PowerShell (current terminal session):**

```powershell
$env:DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=campus_helpdesk_db;encrypt=true;trustServerCertificate=true"
$env:DB_USERNAME = "your_sql_server_username"
$env:DB_PASSWORD = "your_sql_server_password"

$env:MAIL_HOST = "smtp.gmail.com"
$env:MAIL_PORT = "587"
$env:MAIL_USERNAME = "your_email@example.com"
$env:MAIL_PASSWORD = "your_smtp_app_password"
```

**macOS/Linux (current terminal session):**

```bash
export DB_URL='jdbc:sqlserver://localhost:1433;databaseName=campus_helpdesk_db;encrypt=true;trustServerCertificate=true'
export DB_USERNAME='your_sql_server_username'
export DB_PASSWORD='your_sql_server_password'

export MAIL_HOST='smtp.gmail.com'
export MAIL_PORT='587'
export MAIL_USERNAME='your_email@example.com'
export MAIL_PASSWORD='your_smtp_app_password'
```

These variables apply to the terminal session where you run the application. Configure them in your IDE's Run/Debug environment if launching from an IDE.

For Gmail SMTP, use an app password where required by your Google account settings. Do not use your normal account password.

> **Important:** The environment-variable configuration above only works after you replace the existing hard-coded datasource and mail settings in `application.properties` with the placeholder-based configuration shown above.

## Run the Application

Run these commands from the project root (the directory containing `pom.xml`).

**Windows PowerShell or Command Prompt:**

```powershell
.\mvnw.cmd spring-boot:run
```

**macOS/Linux:**

```bash
chmod +x mvnw
./mvnw spring-boot:run
```

The first run may take a few minutes while Maven downloads dependencies.

When Spring Boot starts successfully, open:

- Home page: <http://localhost:8080/>
- Login page: <http://localhost:8080/login>

Stop the server with `Ctrl+C` in the terminal.

## Build and Test

Run the test suite:

**Windows:**

```powershell
.\mvnw.cmd test
```

**macOS/Linux:**

```bash
./mvnw test
```

Package the application:

**Windows:**

```powershell
.\mvnw.cmd clean package
```

**macOS/Linux:**

```bash
./mvnw clean package
```

The generated JAR is normally placed in `target/`. Run it with:

```bash
java -jar target/CampusHelpDesk-1.0-SNAPSHOT.jar
```

If the generated JAR has a different filename, use the actual JAR filename shown in `target/`.

## User Roles

The application code includes role-based flows for:

- **STUDENT** — student ticket dashboard and personal ticket workflows.
- **STAFF** — staff ticket dashboard and ticket handling.
- **ADMIN** — administrative dashboard and management workflows.
- **SUPER_ADMIN** — elevated administration workflows.

The repository does not provide a verified set of default login credentials in this README. Use the project's intended account creation/initialization process or ask the project maintainer for valid development credentials. Do not publish real credentials in the repository.

## Project Structure

```text
.
├── .mvn/wrapper/                  # Maven Wrapper configuration
├── mvnw                           # Maven Wrapper for macOS/Linux
├── mvnw.cmd                       # Maven Wrapper for Windows
├── pom.xml                        # Maven dependencies and build configuration
└── src
    ├── main
    │   ├── java/com/campus/helpdesk
    │   │   ├── config/            # Web configuration and interceptors
    │   │   ├── controller/        # MVC controllers and request handling
    │   │   ├── model/             # JPA entities
    │   │   ├── repository/        # Data access repositories
    │   │   ├── service/           # Application/business logic
    │   │   ├── util/              # Utilities, including email support
    │   │   └── HelpdeskApplication.java
    │   └── resources
    │       ├── application.properties
    │       └── templates/         # Thymeleaf HTML pages
    └── test/                      # Automated tests
```

## Troubleshooting

### Database connection failed

- Confirm SQL Server is running and listening on the configured host and port.
- Confirm the database `campus_helpdesk_db` exists.
- Check the JDBC URL, username, and password.
- Confirm SQL Server TCP/IP connectivity and authentication are enabled as appropriate.
- Review the console output for the underlying JDBC error.

### Application fails to start because of Java

Check `java -version` and make sure Java 21 is installed and selected in your `PATH`. If your IDE uses a different JDK from your terminal, update the IDE project SDK and run configuration.

### Email notifications are not sent

- Check the SMTP host, port, username, and app password.
- Confirm that SMTP access is permitted for the account.
- Check application logs for mail errors.
- The application can start without successful external email delivery only if its runtime configuration and code paths do not require a successful send at startup.

### Port 8080 is already in use

Stop the other process using port 8080 or add this setting to `application.properties`:

```properties
server.port=8081
```

Then open <http://localhost:8081/>.

## Security Notes

- **Never commit database passwords, SMTP passwords, API keys, or real user data.**
- Before publishing this project, remove any credentials that may be present in `application.properties` or elsewhere in the source.
- If a real password or app password was ever committed or shared, revoke/rotate it immediately. Removing it from the latest commit alone does not remove it from Git history.
- Keep local environment files out of Git. Never commit `.env` files containing secrets.
- Use password hashing, server-side authorization checks, secure session settings, CSRF protection, input validation, and appropriate file-upload restrictions before deploying to a public or production environment.
- Do not expose the development server directly to the public internet.
- Review SQL Server certificate settings and database permissions before production use.

## Contributing

1. Fork the repository.
2. Create a feature branch: `git checkout -b feature/your-feature`.
3. Make your changes and run `.\mvnw.cmd test` (Windows) or `./mvnw test` (macOS/Linux).
4. Commit your changes with a clear message.
5. Push the branch and open a Pull Request.

## License

No license file was included in the inspected project archive. Add a `LICENSE` file and update this section before presenting the repository as open source. Without a license, others generally do not receive explicit permission to reuse, modify, or distribute the code.

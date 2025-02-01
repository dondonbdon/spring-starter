# Project Name: Spring Boot Starter Code

## Overview

This project is a **Spring Boot** based starter template designed to provide a solid foundation for building Java applications with common services and features. It includes robust security and API management with JWT token validation, email service for password resets, and a scheduled task for token blacklisting. The application is configured with **MongoDB** and **Redis** for database support and uses **Groovy** for dependency management. Additionally, it is ready for deployment with **Docker Compose**.

---

## 🚀 Features

### 🌱 **Core Technology Stack**
- **Spring Boot** (Java)
- **Groovy** (For dependency management)
- **MongoDB** (For NoSQL database)
- **Redis** (For caching and blacklisting tokens)

### 🔒 **API Security & Features**
- **JWT Authentication**:
    - Secure your routes with JWT tokens.
    - Custom filter to validate user access based on the token's username (user ID).

- **Route Filters**:
    - **Route Existence Filter**: Ensures routes are valid and accessible.
    - **JWT Filter**: Verifies the token on every request to ensure it's valid.
    - **User-Validation Filter**: Ensures that requests to specific routes can only be accessed by the user associated with the token.

### 💼 **Services**
- **Authentication Service**: Manages user login, JWT token generation, and token validation.
- **Email Service**: Sends emails to users for password reset requests.
- **User Service**: Retrieves user information and supports user-related API actions.

### ⚙️ **Scheduled Service**
- **Token Blacklisting**:
    - Blacklists JWT tokens to prevent the use of expired or invalid tokens, even though JWT is stateless.
    - The blacklisting process ensures tokens that are no longer valid (e.g., revoked) will not be accepted, improving security.

### 🛠 **Exception Handling**
- Proper exception handling has been implemented to ensure that errors are properly managed and informative responses are returned to the client.

---

## 🧰 Setup & Configuration

### 🖥 **Development Environment**

1. **Clone the repository:**

   ```bash
   git clone https://github.com/your-username/your-repository.git
   cd your-repository

### 🧰 Setup & Configuration

### 🖥 **Development Environment**

2. **Dependencies**:

   Ensure that you have the following dependencies set up in your development environment:

    - **Java 17+**: This project requires Java 17 or later to run. You can download the JDK from [Oracle](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html) or [AdoptOpenJDK](https://adoptopenjdk.net/).

    - **Groovy**: Groovy is used for dependency management. Install it from [here](https://groovy-lang.org/download.html).

    - **MongoDB**: This project uses MongoDB for storing user data. Make sure MongoDB is running locally or configure it to connect to a remote server. You can download MongoDB from [here](https://www.mongodb.com/try/download/community).

    - **Redis**: Redis is used for caching and blacklisting tokens. Ensure that Redis is installed and running on your local machine or connect to a remote Redis server. You can download Redis from [here](https://redis.io/download).

    - **SMTP Server**: The email service requires an SMTP server for sending password reset emails. You can use a third-party email provider like SendGrid, Mailgun, or configure your own SMTP server.

---

### 📦 **Dependency Management (Groovy)**

This project uses **Groovy** to manage dependencies. Ensure that you have Groovy set up as part of your build system for smooth dependency management. You can configure Groovy by adding it to your `build.gradle` or `pom.xml` file, depending on whether you're using Gradle or Maven.

---

### 🌍 **Running the Application**

1. **Docker Deployment**:
   This project is set up with **Docker Compose** for easy deployment. To start the application, simply run the following command:

   ```bash
   docker-compose up --build

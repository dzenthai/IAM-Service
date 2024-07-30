# IAM-Service

## **Description**

This service is responsible for authentication, authorization, and user registration with subsequent verification.

---

## **Key Features**

- **User Authentication**: Utilizes JWT access tokens for secure authentication.
- **User Registration**: Sends a message to the Notification-Service, followed by email and code verification.
- **Limited Attempts for Registration or Verification**: If a user fails validation (3 attempts), the server will notify
  the user of the error and enforce a waiting period before allowing further attempts.

---

## **Technologies**

- **Spring Boot**: The primary framework for developing the service.
- **Spring Security**: For authentication and authorization.
- **MySQL**: Relational database for data storage.
- **Redis**: Caching and temporary data storage.
- **Kafka**: Messaging system for handling registration codes and notifications.
- **Docker**: Containerization of services for easy deployment.

---

## **Installation Guide**

### **Prerequisites**

Ensure Docker is installed on your system.

### **Steps to Install and Run**

1. **Clone the Repository**
   ```bash
   git clone <repository-url>
   cd security-service

2. **Build the Docker Image**
   Create a Docker image for your service:
   ```bash
   docker build -t security-service .
   
3. **Run Docker Compose**
      If you have a docker-compose.yml, start it to deploy all necessary services:
   ```bash
   docker-compose up

4. **Access the Application**
Once the Docker containers are running, access the application at http://localhost:8080.

5. **Initialize the Database**
The application will automatically create necessary tables in the database on startup. If needed, you can manually initialize the database using the provided SQL script if available.

6. **Configure Application Properties**
After installation, configure the application properties (e.g., application.properties or application.yml) to set environment-specific parameters such as database URLs, tokens, etc.

---

## **Additional Information**

- **Security Best Practices**: Make sure to regularly update your dependencies and monitor for security vulnerabilities.
- **Monitoring and Logging**: Consider setting up monitoring and logging tools to keep track of service performance and issues.
- **Scalability**: For production environments, ensure that you have proper scaling strategies in place for your service and associated dependencies.

## **Conclusion**

This service provides robust user authentication and registration functionalities, integrated with notification systems and has mechanisms in place to handle limited attempts for increased security.




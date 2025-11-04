🚀 Spring Boot Employee Management API

A production-ready Spring Boot CRUD project demonstrating advanced business logic, layered architecture, and modern Spring features such as Actuator, Global Exception Handling, and DTO-based validation.

This project manages employees, allowing operations such as create, read, update, delete, and compute advanced salary logic based on business rules.

🧩 Tech Stack
Component	Technology
Backend Framework	Spring Boot 3.3.4
Language	Java 17
Database	H2 (In-memory)
ORM	Spring Data JPA
Build Tool	Gradle
Lombok	For boilerplate code reduction
Actuator	For health & monitoring endpoints
🏗️ Project Structure
springboot-crud-demo/
 ┣ src/
 ┃ ┗ main/
 ┃   ┣ java/com/example/crud/
 ┃   ┃ ┣ controller/          # REST API endpoints
 ┃   ┃ ┣ dto/                 # Data Transfer Objects
 ┃   ┃ ┣ exception/           # Custom exceptions + global handlers
 ┃   ┃ ┣ model/               # JPA entities
 ┃   ┃ ┣ repository/          # JPA repositories
 ┃   ┃ ┣ service/             # Business logic & transactions
 ┃   ┃ ┗ CrudApplication.java # Main application class
 ┃   ┗ resources/
 ┃     ┣ application.properties # App configuration (DB + Actuator)
 ┣ build.gradle                 # Gradle dependencies & plugins
 ┗ README.md                    # Project documentation

⚙️ Setup Instructions
🧾 Prerequisites

Java 17+

Gradle or IDE with Gradle support (IntelliJ, Eclipse, VS Code)

▶️ Steps to Run
# 1️⃣ Clone the repository
git clone https://github.com/yourusername/springboot-crud-demo.git
cd springboot-crud-demo

# 2️⃣ Build the project
gradle clean build

# 3️⃣ Run the Spring Boot app
gradle bootRun


Application starts on http://localhost:8080

🧠 Core Features
1️⃣ CRUD Operations for Employees

Create Employee: POST /api/employees

Get All Employees: GET /api/employees

Get Employee by ID: GET /api/employees/{id}

Update Employee: PUT /api/employees/{id}

Delete Employee: DELETE /api/employees/{id}

2️⃣ Advanced Business Logic

Each employee’s final salary is computed dynamically based on:

Department-specific bonuses

Tax deductions

Validation checks

private double calculateNetSalary(double baseSalary, String department) {
    double bonusPercentage = switch (department.toLowerCase()) {
        case "engineering" -> 0.15;
        case "hr" -> 0.10;
        case "sales" -> 0.20;
        default -> 0.05;
    };
    double tax = baseSalary * 0.10;
    double bonus = baseSalary * bonusPercentage;
    return baseSalary + bonus - tax;
}

3️⃣ High-Earner Reporting API

Fetch all employees earning above a given threshold:

GET /api/employees/high-earners?minSalary=80000

4️⃣ Global Exception Handling

Centralized exception management using @RestControllerAdvice.

Handles ResourceNotFoundException, IllegalArgumentException, and all other exceptions gracefully.

Returns meaningful error responses:

{
  "timestamp": "2025-11-05T14:22:36.541",
  "message": "Employee not found with ID: 100",
  "details": "uri=/api/employees/100"
}

5️⃣ Actuator for Monitoring

Spring Boot Actuator endpoints:

Endpoint	Description
/actuator	Lists all available endpoints
/actuator/health	Shows app health
/actuator/info	Displays app info
/actuator/metrics	Shows performance metrics

You can enable/disable endpoints in application.properties.

📊 Example API Requests (Using cURL)
➕ Create Employee
curl -X POST http://localhost:8080/api/employees \
-H "Content-Type: application/json" \
-d '{"name":"Anjan Pradhan","department":"Engineering","salary":90000}'

🔍 Get All Employees
curl http://localhost:8080/api/employees

✏️ Update Employee
curl -X PUT http://localhost:8080/api/employees/1 \
-H "Content-Type: application/json" \
-d '{"name":"Anjan P","department":"Sales","salary":95000}'

🧩 Business Rules Summary
Department	Bonus %	Tax %	Remarks
Engineering	15%	10%	Tech employees
HR	10%	10%	Internal operations
Sales	20%	10%	Incentive-driven
Others	5%	10%	Default category
🧰 Tools Used

Spring Boot DevTools for hot reload

Lombok for boilerplate reduction

Spring Data JPA for ORM

Actuator for production metrics

H2 Database Console: http://localhost:8080/h2-console

JDBC URL: jdbc:h2:mem:testdb

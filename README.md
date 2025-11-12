# 🧠 To-Do App Backend (Spring Boot + MySQL)

Simple RESTful API for a To-Do application built with **Spring Boot** and **MySQL**.

## 🚀 Tech Stack
- Java 17+
- Spring Boot 3+
- Spring Data JPA (Hibernate)
- MySQL
- Maven

## ⚙️ Setup
1. Clone the repo:
   ```bash
   git clone https://github.com/<your-username>/<backend-repo>.git
   cd <backend-repo>
Configure your DB in application.properties:

properties
Copy code
spring.datasource.url=jdbc:mysql://localhost:3306/todo_db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
spring.jpa.hibernate.ddl-auto=update
Run the app:

bash
Copy code
mvn spring-boot:run
Server: http://localhost:8080

🔗 API Endpoints
Method	Endpoint	Description

GET	/api/todos	- Get all tasks

POST	/api/todos -	Create task

PUT	/api/todos/{id}  -	Update task

DELETE	/api/todos/{id}	- Soft delete

POST	/api/todos/{id}/restore -	Restore task

GET	/api/todos/completed -	Completed tasks

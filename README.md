# Build project
mvn clean install

# Run application
mvn spring-boot:run

## Access
- API: http://localhost:8080

## Database
- H2 (file-based)
- Auto-created on startup 

---

## 📌 API Endpoints

| Method | Endpoint        | Description                          |
|--------|----------------|--------------------------------------|
| POST   | /tasks         | Create task                          |
| GET    | /tasks/{id}    | Get task                             |
| GET    | /tasks         | List tasks (pagination + filter)     |
| PUT    | /tasks/{id}    | Update task                          |
| DELETE | /tasks/{id}    | Delete task                          |

---

## 🧪 Run Tests

```bash
mvn test    
# 📝 ToDoList Application - Tài liệu hướng dẫn

## 📖 Giới thiệu

ToDoList là một ứng dụng quản lý công việc (Todo List) được xây dựng bằng **Spring Boot**, cho phép người dùng tạo, xem, cập nhật và xóa các công việc của mình. Ứng dụng có hệ thống xác thực và phân quyền dựa trên **JWT (JSON Web Token)**, hỗ trợ 2 vai trò: **USER** và **ADMIN**.

## 🎯 Tính năng chính

- ✅ **Xác thực người dùng**: Đăng ký, đăng nhập với JWT
- ✅ **Phân quyền**: USER và ADMIN với các quyền hạn khác nhau
- ✅ **Quản lý ToDo**: CRUD đầy đủ cho công việc
- ✅ **Phân trang**: Hỗ trợ phân trang khi xem danh sách ToDo
- ✅ **Bảo mật**: Mã hóa mật khẩu bằng BCrypt, bảo vệ API bằng JWT

## 🛠️ Công nghệ sử dụng

- **Java**: 21
- **Spring Boot**: 3.5.7
- **Spring Security**: Xác thực và phân quyền
- **Spring Data JPA**: Làm việc với database
- **MySQL**: Cơ sở dữ liệu
- **JWT (jjwt)**: Xác thực token
- **Lombok**: Giảm boilerplate code
- **Maven**: Quản lý dependencies

## 📋 Yêu cầu hệ thống

- **JDK**: 21 hoặc cao hơn
- **Maven**: 3.6+ 
- **MySQL**: 8.0+
- **IDE**: IntelliJ IDEA, Eclipse, hoặc VS Code (khuyến nghị)

## 🚀 Cài đặt và cấu hình

### 1. Clone repository

```bash
git clone <repository-url>
cd ToDoList
```

### 2. Cấu hình Database

#### Tạo database MySQL:

```sql
CREATE DATABASE todo_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

#### Cấu hình kết nối database

Mở file `src/main/resources/application.yml` và cập nhật thông tin kết nối:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/todo_db
    username: root              # Thay đổi username của bạn
    password: "your_password"   # Thay đổi password của bạn
    driver-class-name: com.mysql.cj.jdbc.Driver
```

### 3. Khởi tạo dữ liệu mẫu (Tùy chọn)

File `src/main/resources/data.sql` chứa script SQL để tạo dữ liệu mẫu. Bạn có thể:

- **Bỏ comment** các dòng trong file `data.sql` để tự động tạo:
  - 2 roles: `USER` và `ADMIN`
  - 1 user admin: username `admin`, password `admin123`
  - 1 user thường: username `user`, password `user123`
  - Một số ToDo mẫu

Hoặc tạo thủ công thông qua các API đăng ký.

### 4. Build project

```bash
# Sử dụng Maven wrapper
./mvnw clean install

# Hoặc nếu đã cài Maven
mvn clean install
```

## ▶️ Chạy ứng dụng

### Cách 1: Sử dụng Maven

```bash
./mvnw spring-boot:run
```

### Cách 2: Sử dụng IDE

1. Mở project trong IDE (IntelliJ IDEA/Eclipse)
2. Mở file `ToDoListApplication.java`
3. Click chuột phải và chọn **Run** hoặc nhấn `Shift + F10`

### Cách 3: Chạy JAR file

```bash
./mvnw clean package
java -jar target/ToDoList-0.0.1-SNAPSHOT.jar
```

Ứng dụng sẽ chạy tại: **http://localhost:8080**

## 📁 Cấu trúc Project

```
ToDoList/
├── src/
│   ├── main/
│   │   ├── java/com/example/todolist/
│   │   │   ├── config/
│   │   │   │   └── SecurityConfig.java          # Cấu hình Security
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java          # API xác thực
│   │   │   │   ├── ToDoController.java          # API ToDo
│   │   │   │   └── UserController.java          # API User
│   │   │   ├── DTOs/
│   │   │   │   ├── AuthRequest.java             # DTO đăng nhập
│   │   │   │   ├── AuthResponse.java            # DTO phản hồi JWT
│   │   │   │   └── RegisterRequest.java         # DTO đăng ký
│   │   │   ├── entity/
│   │   │   │   ├── User.java                    # Entity User
│   │   │   │   ├── Role.java                    # Entity Role
│   │   │   │   └── ToDo.java                    # Entity ToDo
│   │   │   ├── repository/
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── RoleRepository.java
│   │   │   │   └── ToDoRepository.java
│   │   │   ├── security/
│   │   │   │   ├── JwtUtil.java                 # Utility JWT
│   │   │   │   ├── JwtAuthenticationFilter.java # Filter xác thực JWT
│   │   │   │   ├── CustomUserDetail.java
│   │   │   │   └── CustomUserDetailsService.java
│   │   │   ├── service/
│   │   │   │   ├── UserService.java
│   │   │   │   └── ToDoService.java
│   │   │   └── ToDoListApplication.java         # Main class
│   │   └── resources/
│   │       ├── application.yml                   # Cấu hình ứng dụng
│   │       └── data.sql                          # Dữ liệu mẫu (tùy chọn)
│   └── test/
├── pom.xml                                       # Maven dependencies
└── README.md                                     # Tài liệu này
```

## 🔌 API Endpoints

### 🔐 Authentication APIs

#### 1. Đăng ký tài khoản

```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "newuser",
  "password": "password123",
  "email": "user@example.com"
}
```

**Response:**
- `200 OK`: "User registered successfully"
- `400 Bad Request`: "Username already exists"

#### 2. Đăng nhập

```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

Lưu token này để sử dụng cho các API yêu cầu xác thực.

### 📝 ToDo APIs

**Lưu ý:** Tất cả các API ToDo đều yêu cầu JWT token trong header:

```http
Authorization: Bearer <your-jwt-token>
```

#### 1. Lấy danh sách ToDo của user (có phân trang)

```http
GET /api/todos/user/{userId}?page=0&size=10
Authorization: Bearer <token>
```

**Quyền:**
- **USER**: Chỉ xem được ToDo của chính mình
- **ADMIN**: Xem được ToDo của mọi user

**Response:**
```json
{
  "content": [
    {
      "id": 1,
      "title": "Học Spring Security",
      "desciption": "Học cách cấu hình JWT",
      "status": "PENDING",
      "dueDate": "2025-11-05",
      "createdAt": "2024-11-01T10:00:00",
      "updatedAt": "2024-11-01T10:00:00"
    }
  ],
  "totalElements": 10,
  "totalPages": 1,
  "page": 0,
  "size": 10
}
```

#### 2. Tạo ToDo mới

```http
POST /api/todos/user/{userId}
Authorization: Bearer <token>
Content-Type: application/json

{
  "title": "Học React",
  "desciption": "Học React hooks và state management",
  "status": "PENDING",
  "dueDate": "2025-12-01"
}
```

**Quyền:**
- **USER**: Chỉ tạo được ToDo cho chính mình
- **ADMIN**: Tạo được ToDo cho bất kỳ user nào

**Trạng thái (status):** `PENDING`, `IN_PROGRESS`, `COMPLETED`

#### 3. Cập nhật ToDo

```http
PUT /api/todos/{todoId}
Authorization: Bearer <token>
Content-Type: application/json

{
  "title": "Học React - Updated",
  "desciption": "Đã hoàn thành",
  "status": "COMPLETED",
  "dueDate": "2025-12-01"
}
```

**Quyền:**
- **USER**: Chỉ cập nhật được ToDo của chính mình
- **ADMIN**: Cập nhật được mọi ToDo

#### 4. Xóa ToDo

```http
DELETE /api/todos/{todoId}
Authorization: Bearer <token>
```

**Quyền:**
- **USER**: Chỉ xóa được ToDo của chính mình
- **ADMIN**: Xóa được mọi ToDo

**Response:**
- `204 No Content`: Xóa thành công
- `403 Forbidden`: Không có quyền
- `404 Not Found`: Không tìm thấy ToDo

### 👤 User APIs

#### 1. Lấy thông tin user hiện tại

```http
GET /api/users/me
Authorization: Bearer <token>
```

#### 2. Lấy danh sách tất cả users (chỉ ADMIN)

```http
GET /api/users/allUsers
Authorization: Bearer <admin-token>
```

## 🔒 Bảo mật và Phân quyền

### Vai trò (Roles)

1. **USER**: 
   - Quản lý ToDo của chính mình
   - Xem thông tin cá nhân

2. **ADMIN**:
   - Tất cả quyền của USER
   - Xem tất cả users
   - Quản lý ToDo của mọi user

### JWT Token

- Token được tạo khi đăng nhập thành công
- Thời gian sống: Được cấu hình trong `JwtUtil.java`
- Format: `Bearer <token>` trong header Authorization

### Bảo mật

- Mật khẩu được mã hóa bằng **BCrypt**
- API được bảo vệ bởi **Spring Security**
- Session stateless (sử dụng JWT)

## 📝 Ví dụ sử dụng với cURL

### 1. Đăng ký

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "test123",
    "email": "test@example.com"
  }'
```

### 2. Đăng nhập

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
```

Lưu token từ response.

### 3. Tạo ToDo

```bash
curl -X POST http://localhost:8080/api/todos/user/1 \
  -H "Authorization: Bearer <your-token>" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Hoàn thành dự án",
    "desciption": "Làm xong API documentation",
    "status": "IN_PROGRESS",
    "dueDate": "2025-12-31"
  }'
```

### 4. Lấy danh sách ToDo

```bash
curl -X GET "http://localhost:8080/api/todos/user/1?page=0&size=10" \
  -H "Authorization: Bearer <your-token>"
```

## 🧪 Testing

Chạy test:

```bash
./mvnw test
```

## 🐛 Xử lý lỗi thường gặp

### 1. Lỗi kết nối database

```
Could not connect to database
```

**Giải pháp:**
- Kiểm tra MySQL đã chạy chưa
- Kiểm tra thông tin kết nối trong `application.yml`
- Đảm bảo database `todo_db` đã được tạo

### 2. Lỗi JWT token

```
401 Unauthorized
```

**Giải pháp:**
- Kiểm tra token còn hiệu lực không
- Đảm bảo header Authorization đúng format: `Bearer <token>`
- Thử đăng nhập lại để lấy token mới

### 3. Lỗi phân quyền

```
403 Forbidden
```

**Giải pháp:**
- USER chỉ có thể thao tác với ToDo của chính mình
- Kiểm tra role của user có phù hợp không

## 🔧 Cấu hình nâng cao

### Thay đổi thời gian sống của JWT

Mở file `JwtUtil.java` và điều chỉnh:

```java
.setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 24 giờ
```

### Thay đổi port

Thêm vào `application.yml`:

```yaml
server:
  port: 8081
```

### Logging

Thêm vào `application.yml`:

```yaml
logging:
  level:
    com.example.todolist: DEBUG
    org.springframework.security: DEBUG
```

## 📚 Tài liệu tham khảo

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Security Reference](https://docs.spring.io/spring-security/reference/index.html)
- [JWT.io](https://jwt.io/)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)

## 👥 Đóng góp

Mọi đóng góp đều được chào đón! Vui lòng:

1. Fork repository
2. Tạo branch mới (`git checkout -b feature/AmazingFeature`)
3. Commit thay đổi (`git commit -m 'Add some AmazingFeature'`)
4. Push lên branch (`git push origin feature/AmazingFeature`)
5. Tạo Pull Request

## 📄 License

Dự án này là mã nguồn mở và được phân phối dưới giấy phép MIT.

## 📧 Liên hệ

Nếu có thắc mắc hoặc cần hỗ trợ, vui lòng tạo issue trong repository.

---

**Chúc bạn code vui vẻ! 🚀**


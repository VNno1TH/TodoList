# -- =========================================================
# -- 🚀 INITIAL DATA FOR ToDoList APPLICATION
# -- =========================================================
#
# -- 🧩 Tạo Roles
# INSERT INTO roles (name) VALUES ('USER');
# INSERT INTO roles (name) VALUES ('ADMIN');
#
# -- =========================================================
# -- 👤 Tạo người dùng ADMIN mẫu
# -- Mật khẩu: admin123 (BCrypt)
# -- =========================================================
# INSERT INTO users (username, password, email, created_at)
# VALUES (
#            'admin',
#            '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a',
#            'admin@example.com',
#            NOW()
#        );
#
# -- Gán Role ADMIN cho user admin
# INSERT INTO user_role (user_id, role_id)
# VALUES (
#            (SELECT id FROM users WHERE username = 'admin'),
#            (SELECT id FROM roles WHERE name = 'ADMIN')
#        );
#
# -- =========================================================
# -- 👤 Tạo người dùng thường mẫu
# -- Mật khẩu: user123 (BCrypt)
# -- =========================================================
# INSERT INTO users (username, password, email, created_at)
# VALUES (
#            'user',
#            '$2a$10$N9qo8uLOickgx2ZMRZoMye/IjEfq6XLIQ/6OBvjvPWe5mPAX2L2g6',
#            'user@example.com',
#            NOW()
#        );
#
# -- Gán Role USER cho user thường
# INSERT INTO user_role (user_id, role_id)
# VALUES (
#            (SELECT id FROM users WHERE username = 'user'),
#            (SELECT id FROM roles WHERE name = 'USER')
#        );
#
# -- =========================================================
# -- ✅ Tạo vài ToDo mẫu cho mỗi người dùng
# -- =========================================================
# INSERT INTO todos (title, desciption, status, due_date, created_at, updated_at, user_id)
# VALUES
#     ('Học Spring Security', 'Học cách cấu hình JWT trong Spring Boot', 'PENDING', '2025-11-05', NOW(), NOW(),
#      (SELECT id FROM users WHERE username = 'user')),
#
#     ('Học JPA', 'Ôn lại kiến thức JPA ManyToMany', 'COMPLETED', '2025-11-02', NOW(), NOW(),
#      (SELECT id FROM users WHERE username = 'user')),
#
#     ('Quản lý ToDo', 'Tạo CRUD API cho ToDo List', 'IN_PROGRESS', '2025-11-10', NOW(), NOW(),
#      (SELECT id FROM users WHERE username = 'admin'));

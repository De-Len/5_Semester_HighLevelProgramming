-- Тестовые пользователи
INSERT INTO users (username, password_hash, salt) VALUES
('user1', 'hash1_placeholder', 'salt1_placeholder'),
('user2', 'hash2_placeholder', 'salt2_placeholder');

-- Ресурсы
INSERT INTO resources (path, volume) VALUES
('A', 100),
('A.B', 50),
('A.B.C', 20),
('D.E', 10);

-- Разрешения
INSERT INTO permissions (username, resource_path, role) VALUES
('user1', 'A', 'READ'),
('user1', 'A.B', 'WRITE'),
('user2', 'A.B.C', 'EXECUTE');
-- Users
INSERT INTO users (login, password_hash, salt) VALUES
('user1', 'IssOwq8laW1VuGMoeOyoYpc1reF3vTGGE2e087+Vweo=', 'c2FsdDE='),
('user2', 'P10BcVQK0ddrXEHzBOl9I0x+/JTmyPrgi7vYWwKu+0w=', 'c2FsdDI=');

-- Resources
INSERT INTO resources (path, max_volume) VALUES
('A', 100),
('A.B', 50),
('A.B.C', 20),
('D.E', 10);

-- Permissions
INSERT INTO permissions (user_login, resource_path, role) VALUES
('user1', 'A', 'READ'),
('user1', 'A.B', 'WRITE'),
('user2', 'A.B.C', 'EXECUTE');
CREATE TABLE IF NOT EXISTS users (
    login VARCHAR(50) PRIMARY KEY,
    password_hash VARCHAR(255) NOT NULL,
    salt VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS resources (
    path VARCHAR(255) PRIMARY KEY,
    volume INT NOT NULL
);

CREATE TABLE IF NOT EXISTS permissions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_login VARCHAR(50) NOT NULL,
    resource_path VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    FOREIGN KEY (user_login) REFERENCES users(login) ON DELETE CASCADE,
    FOREIGN KEY (resource_path) REFERENCES resources(path) ON DELETE CASCADE
);
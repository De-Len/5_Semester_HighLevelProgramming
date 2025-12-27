CREATE TABLE users (
    login VARCHAR(255) PRIMARY KEY,
    password_hash VARCHAR(255) NOT NULL,
    salt VARCHAR(255) NOT NULL
);

CREATE TABLE resources (
    path VARCHAR(255) PRIMARY KEY,
    max_volume INT NOT NULL
);

CREATE TABLE permissions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_login VARCHAR(255) NOT NULL,
    resource_path VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL
);
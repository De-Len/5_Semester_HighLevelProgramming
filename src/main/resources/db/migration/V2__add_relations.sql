ALTER TABLE permissions
ADD CONSTRAINT fk_permission_user FOREIGN KEY (user_login) REFERENCES users(login);

ALTER TABLE permissions
ADD CONSTRAINT fk_permission_resource FOREIGN KEY (resource_path) REFERENCES resources(path);

CREATE INDEX idx_permissions_user_resource ON permissions(user_login, resource_path);
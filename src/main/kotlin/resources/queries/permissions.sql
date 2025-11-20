-- name: get_all_permissions
SELECT username, resource_path, role FROM permissions;

-- name: get_user_permissions
SELECT username, resource_path, role FROM permissions WHERE username = ?;

-- name: get_resource_permissions
SELECT username, resource_path, role FROM permissions WHERE resource_path = ?;

-- name: insert_permission
INSERT INTO permissions (username, resource_path, role) VALUES (?, ?, ?);

-- name: update_permission_role
UPDATE permissions SET role = ? WHERE username = ? AND resource_path = ?;

-- name: delete_permission
DELETE FROM permissions WHERE username = ? AND resource_path = ?;

-- name: delete_all_user_permissions
DELETE FROM permissions WHERE username = ?;
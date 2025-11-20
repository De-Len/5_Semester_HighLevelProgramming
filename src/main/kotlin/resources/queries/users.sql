-- name: get_user_by_username
SELECT username, password_hash, salt FROM users WHERE username = ?;

-- name: get_all_users
SELECT username, password_hash, salt FROM users;

-- name: insert_user
INSERT INTO users (username, password_hash, salt) VALUES (?, ?, ?);

-- name: update_user_password
UPDATE users SET password_hash = ?, salt = ? WHERE username = ?;

-- name: delete_user
DELETE FROM users WHERE username = ?;
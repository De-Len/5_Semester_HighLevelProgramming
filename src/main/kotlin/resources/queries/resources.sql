-- name: get_all_resources
SELECT path, volume FROM resources;

-- name: get_resource_by_path
SELECT path, volume FROM resources WHERE path = ?;

-- name: insert_resource
INSERT INTO resources (path, volume) VALUES (?, ?);

-- name: update_resource_volume
UPDATE resources SET volume = ? WHERE path = ?;

-- name: delete_resource
DELETE FROM resources WHERE path = ?;
DELETE FROM "UserType";
DELETE FROM "Permissions";
DELETE FROM "UserTypePermissions";

INSERT INTO "UserType" (type_name)
VALUES ('admin'), ('instructor'), ('student'), ('user');

INSERT INTO "Permissions" (permission_name)
VALUES ('manage_users'), ('enroll'), ('create_course'),
       ('edit_course'), ('delete_course');

WITH admin_id AS (
    SELECT user_type_id AS id FROM "UserType" AS ut WHERE ut.type_name = 'admin'
), instructor_id AS (
    SELECT user_type_id AS id FROM "UserType" AS ut WHERE ut.type_name = 'instructor'
), student_id AS (
    SELECT user_type_id AS id FROM "UserType" AS ut WHERE ut.type_name = 'student'
), perms AS (
    SELECT permission_id AS id, permission_name AS n FROM "Permissions"
)
INSERT INTO "UserTypePermissions" (user_type_id, permission_id)
VALUES
    ((SELECT id FROM admin_id), (SELECT id FROM perms WHERE n = 'manage_users')),
    ((SELECT id FROM instructor_id), (SELECT id FROM perms WHERE n = 'create_course')),
    ((SELECT id FROM instructor_id), (SELECT id FROM perms WHERE n = 'edit_course')),
    ((SELECT id FROM instructor_id), (SELECT id FROM perms WHERE n = 'delete_course')),
    ((SELECT id FROM student_id), (SELECT id FROM perms WHERE n = 'enroll'));

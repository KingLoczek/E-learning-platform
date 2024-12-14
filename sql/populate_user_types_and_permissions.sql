DELETE FROM "UserType";
DELETE FROM "Permissions";
DELETE FROM "UserTypePermissions";

INSERT INTO "UserType" (type_name)
VALUES ('admin'), ('instructor'), ('student'), ('user');

INSERT INTO "Permissions" (permission_name)
VALUES ('manage_users'), ('enroll'), ('create_course'),
       ('edit_course'), ('delete_course'), ('manage_all_files'),
       ('create_topic'), ('edit_topic'), ('delete_topic'),
       ('create_material'), ('edit_material'), ('delete_material'),
       ('create_assignment'), ('edit_assignment'), ('delete_assignment'),
       ('create_submission'), ('edit_submission'), ('delete_submission'), ('manage_course_submissions'),
       ('create_grade'), ('edit_grade'), ('delete_grade'),
       ('create_event'), ('edit_event'), ('delete_event'),
       ('create_file'), ('edit_file'), ('delete_file');

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
    ((SELECT id FROM admin_id), (SELECT id FROM perms WHERE n = 'manage_all_files')),
    ((SELECT id FROM admin_id), (SELECT id FROM perms WHERE n = 'create_file')),
    ((SELECT id FROM admin_id), (SELECT id FROM perms WHERE n = 'edit_file')),
    ((SELECT id FROM admin_id), (SELECT id FROM perms WHERE n = 'delete_file')),
    ((SELECT id FROM instructor_id), (SELECT id FROM perms WHERE n = 'create_course')),
    ((SELECT id FROM instructor_id), (SELECT id FROM perms WHERE n = 'edit_course')),
    ((SELECT id FROM instructor_id), (SELECT id FROM perms WHERE n = 'delete_course')),
    ((SELECT id FROM instructor_id), (SELECT id FROM perms WHERE n = 'create_topic')),
    ((SELECT id FROM instructor_id), (SELECT id FROM perms WHERE n = 'edit_topic')),
    ((SELECT id FROM instructor_id), (SELECT id FROM perms WHERE n = 'delete_topic')),
    ((SELECT id FROM instructor_id), (SELECT id FROM perms WHERE n = 'create_material')),
    ((SELECT id FROM instructor_id), (SELECT id FROM perms WHERE n = 'edit_material')),
    ((SELECT id FROM instructor_id), (SELECT id FROM perms WHERE n = 'delete_material')),
    ((SELECT id FROM instructor_id), (SELECT id FROM perms WHERE n = 'create_assignment')),
    ((SELECT id FROM instructor_id), (SELECT id FROM perms WHERE n = 'edit_assignment')),
    ((SELECT id FROM instructor_id), (SELECT id FROM perms WHERE n = 'delete_assignment')),
    ((SELECT id FROM instructor_id), (SELECT id FROM perms WHERE n = 'manage_course_submissions')),
    ((SELECT id FROM instructor_id), (SELECT id FROM perms WHERE n = 'create_grade')),
    ((SELECT id FROM instructor_id), (SELECT id FROM perms WHERE n = 'edit_grade')),
    ((SELECT id FROM instructor_id), (SELECT id FROM perms WHERE n = 'delete_grade')),
    ((SELECT id FROM instructor_id), (SELECT id FROM perms WHERE n = 'create_event')),
    ((SELECT id FROM instructor_id), (SELECT id FROM perms WHERE n = 'edit_event')),
    ((SELECT id FROM instructor_id), (SELECT id FROM perms WHERE n = 'delete_event')),
    ((SELECT id FROM instructor_id), (SELECT id FROM perms WHERE n = 'create_file')),
    ((SELECT id FROM instructor_id), (SELECT id FROM perms WHERE n = 'edit_file')),
    ((SELECT id FROM instructor_id), (SELECT id FROM perms WHERE n = 'delete_file')),
    ((SELECT id FROM student_id), (SELECT id FROM perms WHERE n = 'enroll')),
    ((SELECT id FROM student_id), (SELECT id FROM perms WHERE n = 'create_submission')),
    ((SELECT id FROM student_id), (SELECT id FROM perms WHERE n = 'edit_submission')),
    ((SELECT id FROM student_id), (SELECT id FROM perms WHERE n = 'delete_submission')),
((SELECT id FROM student_id), (SELECT id FROM perms WHERE n = 'create_file')),
((SELECT id FROM student_id), (SELECT id FROM perms WHERE n = 'edit_file')),
((SELECT id FROM student_id), (SELECT id FROM perms WHERE n = 'delete_file'));

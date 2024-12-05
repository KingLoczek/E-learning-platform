DROP TABLE IF EXISTS "Users" CASCADE;
DROP TABLE IF EXISTS "UserType" CASCADE;
DROP TYPE IF EXISTS "StatusType" CASCADE;
DROP TABLE IF EXISTS "Courses" CASCADE;
DROP TABLE IF EXISTS "Enrollments" CASCADE;
DROP TABLE IF EXISTS "Topics" CASCADE;
DROP TABLE IF EXISTS "Materials" CASCADE;
DROP TABLE IF EXISTS "Assignments" CASCADE;
DROP TABLE IF EXISTS "FileStorage";
DROP TABLE IF EXISTS "Submissions" CASCADE;
DROP TABLE IF EXISTS "Grades";
DROP TABLE IF EXISTS "Events";
DROP TABLE IF EXISTS "MaterialFiles";
DROP TABLE IF EXISTS "AssignmentFiles";
DROP TABLE IF EXISTS "SubmissionFiles";
DROP TABLE IF EXISTS "UserTypePermissions";
DROP TABLE IF EXISTS "FileTypes";
DROP TABLE IF EXISTS "Permissions";


CREATE TABLE "Users" (
  "user_id" INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "first_name" varchar(50),
  "last_name" varchar(50),
  "email" varchar(100) UNIQUE,
  "password" varchar(255),
  "user_type_id" integer
);

CREATE TABLE "UserType" (
  "user_type_id" INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "type_name" varchar(50)
);

CREATE TYPE "StatusType" AS ENUM ('Completed', 'Archived', 'Ongoing');

CREATE TABLE "Courses" (
  "course_id" INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "name" varchar(100),
  "description" text,
  "access_key" varchar(10),
  "instructor_id" integer,
  "status_type" StatusType
);

CREATE TABLE "Enrollments" (
  "enrollment_id" INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "student_id" integer,
  "course_id" integer,
  "joined_at" timestamp with time zone,
  "status_type" StatusType
);

CREATE TABLE "Topics" (
  "topic_id" INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "title" varchar(100),
  "description" text,
  "course_id" integer
);

CREATE TABLE "Materials" (
  "material_id" INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "name" varchar(100),
  "content" text,
  "author_id" integer,
  "topic_id" integer
);

CREATE TABLE "Assignments" (
  "assignment_id" INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "name" varchar(100),
  "content" text,
  "due_date" timestamp with time zone,
  "assigned_by" integer,
  "topic_id" integer
);

CREATE TABLE "FileStorage" (
  "file_id" INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "file_path" varchar(255),
  "file_type_id" integer
);

CREATE TABLE "Submissions" (
  "submission_id" INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "student_id" integer,
  "assignment_id" integer,
  "is_submitted" boolean,
  "submitted_at" timestamp with time zone
);

CREATE TABLE "Grades" (
  "grade_id" INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "submission_id" integer,
  "grade" integer,
  "feedback" text,
  "graded_at" timestamp with time zone,
  "instructor_id" integer
);

CREATE TABLE "Events" (
  "event_id" INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "title" text,
  "start_date" timestamp with time zone,
  "end_date" timestamp with time zone,
  "course_id" integer
);

CREATE TABLE "MaterialFiles" (
  "id" INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "material_id" integer,
  "file_id" integer
);

CREATE TABLE "AssignmentFiles" (
  "id" INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "assignment_id" integer,
  "file_id" integer
);

CREATE TABLE "SubmissionFiles" (
  "id" INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "submission_id" integer,
  "file_id" integer
);

CREATE TABLE "Permissions" (
  "permission_id" INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "permission_name" varchar(50)
);

CREATE TABLE "UserTypePermissions" (
  "user_type_id" integer,
  "permission_id" integer
);

CREATE TABLE "FileTypes" (
  "file_type_id" INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "type_name" varchar(50),
  "mime_type" varchar(100)
);

CREATE TABLE "Users_Enrollments" (
     "Users_user_id" INTEGER,
     "Enrollments_student_id" integer,
     PRIMARY KEY ("Users_user_id", "Enrollments_student_id")
);

ALTER TABLE "UserType" ADD FOREIGN KEY ("user_type_id") REFERENCES "UserTypePermissions" ("user_type_id");

ALTER TABLE "Permissions" ADD FOREIGN KEY ("permission_id") REFERENCES "UserTypePermissions" ("permission_id");

ALTER TABLE "Events" ADD FOREIGN KEY ("course_id") REFERENCES "Courses" ("course_id");

ALTER TABLE "Users" ADD FOREIGN KEY ("user_type_id") REFERENCES "UserType" ("user_type_id");

ALTER TABLE "Grades" ADD FOREIGN KEY ("instructor_id") REFERENCES "Users" ("user_id");

ALTER TABLE "Users" ADD FOREIGN KEY ("user_id") REFERENCES "Courses" ("instructor_id");

ALTER TABLE "Courses" ADD FOREIGN KEY ("course_id") REFERENCES "Enrollments" ("course_id");

ALTER TABLE "Topics" ADD FOREIGN KEY ("course_id") REFERENCES "Courses" ("course_id");

ALTER TABLE "Users_Enrollments" ADD FOREIGN KEY ("Users_user_id") REFERENCES "Users" ("user_id");

ALTER TABLE "Users_Enrollments" ADD FOREIGN KEY ("Enrollments_student_id") REFERENCES "Enrollments" ("student_id");

ALTER TABLE "Users" ADD FOREIGN KEY ("user_id") REFERENCES "Materials" ("author_id");

ALTER TABLE "Users" ADD FOREIGN KEY ("user_id") REFERENCES "Assignments" ("assigned_by");

ALTER TABLE "Submissions" ADD FOREIGN KEY ("student_id") REFERENCES "Users" ("user_id");

ALTER TABLE "Submissions" ADD FOREIGN KEY ("assignment_id") REFERENCES "Assignments" ("assignment_id");

ALTER TABLE "Grades" ADD FOREIGN KEY ("submission_id") REFERENCES "Submissions" ("submission_id");

ALTER TABLE "Materials" ADD FOREIGN KEY ("material_id") REFERENCES "MaterialFiles" ("material_id");

ALTER TABLE "FileStorage" ADD FOREIGN KEY ("file_id") REFERENCES "MaterialFiles" ("file_id");

ALTER TABLE "Assignments" ADD FOREIGN KEY ("assignment_id") REFERENCES "AssignmentFiles" ("assignment_id");

ALTER TABLE "FileStorage" ADD FOREIGN KEY ("file_id") REFERENCES "AssignmentFiles" ("file_id");

ALTER TABLE "Submissions" ADD FOREIGN KEY ("submission_id") REFERENCES "SubmissionFiles" ("submission_id");

ALTER TABLE "FileStorage" ADD FOREIGN KEY ("file_id") REFERENCES "SubmissionFiles" ("file_id");

ALTER TABLE "FileTypes" ADD FOREIGN KEY ("file_type_id") REFERENCES "FileStorage" ("file_type_id");

ALTER TABLE "Assignments" ADD FOREIGN KEY ("topic_id") REFERENCES "Topics" ("topic_id");

ALTER TABLE "Materials" ADD FOREIGN KEY ("topic_id") REFERENCES "Topics" ("topic_id");

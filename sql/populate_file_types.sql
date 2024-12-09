DELETE FROM "FileTypes";

INSERT INTO "FileTypes" (type_name, mime_type, extension)
VALUES ('Text File', 'text/plain', 'txt'),
       ('Java Source File', 'text/plain', 'java'),
       ('C++ Source File', 'text/plain', 'cpp'),
       ('C++ Header File', 'text/plain', 'hpp'),
       ('C Source File', 'text/plain', 'c'),
       ('C Header File', 'text/plain', 'h'),
       ('Portable Document Format', 'application/pdf', 'pdf');
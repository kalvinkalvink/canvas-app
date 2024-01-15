CREATE TABLE IF NOT EXISTS course
(
    id          INT PRIMARY KEY NOT NULL,
    name        VARCHAR(255)    NOT NULL,
    course_code VARCHAR(255)    NOT NULL,
    selected    ENUM ('Y','N')  NOT NULL default 'N'
);
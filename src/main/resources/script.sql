CREATE TABLE users
(
    id       BIGINT AUTO_INCRIMENT PRIMARY KEY,
    username VARCHAR(64),
    password VARCHAR(256),
    email    VARCHAR(128),
    role     VARCHAR(32)
);
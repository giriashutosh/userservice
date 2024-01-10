CREATE TABLE `role`
(
    id     BIGINT AUTO_INCREMENT NOT NULL,
    `role` VARCHAR(255) NULL,
    CONSTRAINT PK_ROLE PRIMARY KEY (id)
);

CREATE TABLE session
(
    id             BIGINT AUTO_INCREMENT NOT NULL,
    expiring_at    datetime NULL,
    session_status TINYINT(3) NULL,
    token          VARCHAR(255) NULL,
    user_id        BIGINT NULL,
    CONSTRAINT PK_SESSION PRIMARY KEY (id)
);

CREATE TABLE user
(
    id       BIGINT AUTO_INCREMENT NOT NULL,
    email    VARCHAR(255) NULL,
    password VARCHAR(255) NULL,
    CONSTRAINT PK_USER PRIMARY KEY (id)
);

CREATE TABLE user_roles
(
    user_id  BIGINT NOT NULL,
    roles_id BIGINT NOT NULL,
    CONSTRAINT PK_USER_ROLES PRIMARY KEY (user_id, roles_id)
);

CREATE INDEX FK1bi1pmqjgipw7dx3j6bl37dja ON session (user_id);

CREATE INDEX FKj9553ass9uctjrmh0gkqsmv0d ON user_roles (roles_id);

ALTER TABLE session
    ADD CONSTRAINT FK1bi1pmqjgipw7dx3j6bl37dja FOREIGN KEY (user_id) REFERENCES user (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE user_roles
    ADD CONSTRAINT FK55itppkw3i07do3h7qoclqd4k FOREIGN KEY (user_id) REFERENCES user (id) ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE user_roles
    ADD CONSTRAINT FKj9553ass9uctjrmh0gkqsmv0d FOREIGN KEY (roles_id) REFERENCES `role` (id) ON UPDATE RESTRICT ON DELETE RESTRICT;
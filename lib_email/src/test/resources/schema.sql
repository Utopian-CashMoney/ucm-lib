--Only create the tables we care about.

-- -----------------------------------------------------
-- Table `users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `users`
(
    `id`         INT          NOT NULL AUTO_INCREMENT,
    `username`   VARCHAR(31)  NOT NULL,
    `email`      VARCHAR(127) NOT NULL,
    `password`   VARCHAR(255) NOT NULL,
    `phone`      VARCHAR(31)  NULL,
    `first_name` VARCHAR(63)  NOT NULL,
    `last_name`  VARCHAR(63)  NOT NULL,
    `is_active`  TINYINT      NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE UNIQUE INDEX `email_UNIQUE` ON `users` (`email` ASC);

CREATE UNIQUE INDEX `username_UNIQUE` ON `users` (`username` ASC);

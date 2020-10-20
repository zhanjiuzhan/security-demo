CREATE TABLE IF NOT EXISTS user (
    username varchar(64) NOT NULL,
    role varchar(32) NOT NULL,
    password varchar(256) NULL DEFAULT '',
    isEnabled bigint NOT NULL DEFAULT 1,
    PRIMARY KEY (username),
    INDEX idx_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

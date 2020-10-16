CREATE TABLE IF NOT EXISTS user (
    username varchar(32) NOT NULL,
    password varchar(32) NULL DEFAULT '',
    isEnabled bigint NOT NULL DEFAULT 1,
    PRIMARY KEY (username),
    INDEX idx_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

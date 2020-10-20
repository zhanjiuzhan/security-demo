# 上述的密码就是 $2a$10$ogqSsM0uWI7m0qx0rMeX1uyTKMPsBt1CcdykTJD3RVEcbd42Z2PMq = abc123  只不过是加密后的

INSERT IGNORE INTO user(username, role, password, isEnabled) values ("admin", "ROLE_ADMIN", "$2a$10$ogqSsM0uWI7m0qx0rMeX1uyTKMPsBt1CcdykTJD3RVEcbd42Z2PMq", 1);
INSERT IGNORE INTO user(username, role, password, isEnabled) values ("user", "ROLE_USER", "$2a$10$ogqSsM0uWI7m0qx0rMeX1uyTKMPsBt1CcdykTJD3RVEcbd42Z2PMq", 1);
INSERT IGNORE INTO user(username, role, password, isEnabled) values ("guest", "ROLE_GUEST", "$2a$10$ogqSsM0uWI7m0qx0rMeX1uyTKMPsBt1CcdykTJD3RVEcbd42Z2PMq", 1);

package org.demo.security.dao.impl;

import org.demo.security.dao.UserDao;
import org.demo.security.model.User;
import org.springframework.stereotype.Repository;


/**
 * @author Administrator
 */
@Repository
public class UserDaoMysqlImpl implements UserDao {

    @Override
    public User getByUsername(String username) {
        User user = new User(username, "abc123");
        return user;
    }
}

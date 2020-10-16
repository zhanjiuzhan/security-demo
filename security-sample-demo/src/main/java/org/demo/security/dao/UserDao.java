package org.demo.security.dao;

import org.demo.security.model.User;

/**
 * @author Administrator
 */
public interface UserDao {

    /**
     * 根据用户名取得用户对象
     * @param username
     * @return
     */
    User getByUsername(String username);
}

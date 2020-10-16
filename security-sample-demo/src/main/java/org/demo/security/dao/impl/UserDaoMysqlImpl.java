package org.demo.security.dao.impl;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.demo.security.dao.UserDao;
import org.demo.security.model.User;

/**
 * @author Administrator
 */
@Mapper
public interface UserDaoMysqlImpl extends UserDao {

    String TABLE_NAME = "user";

    /**
     * 根据用户名取得用户对象
     * @param username
     * @return
     */
    @Override
    @Select("select * from " + TABLE_NAME + " where username = #{username}")
    User getByUsername(String username);
}

package org.demo.security.service.impl;

import org.demo.security.dao.impl.UserDaoMysqlImpl;
import org.demo.security.model.User;
import org.demo.security.service.UserDetailService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;


/**
 * @author Administrator
 */
@Service
public class UserDetailServiceImpl implements UserDetailService {

    @Resource
    private UserDaoMysqlImpl userDaoMysql;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        if (s != null) {
            User user = userDaoMysql.getByUsername(s);
            if (user != null) {
                Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>(1);
                // 至于为什么数据库中是ROLE_XXX 而设置角色的时候是XXX 这个是Security角色判定里面实现的
                grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole()));
                user.setGrantedAuthorities(grantedAuthorities);
                return user;
            }
        }
        return new User();
    }
}

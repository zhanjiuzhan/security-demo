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
import java.util.Collections;


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
                grantedAuthorities.add(new SimpleGrantedAuthority("ADMIN"));
                user.setGrantedAuthorities(grantedAuthorities);
                return user;
            }
        }
        return new User();
    }
}

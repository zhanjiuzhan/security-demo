package org.demo.security.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 */
@Component
public class MySpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/welcome")
                    .permitAll()
                .antMatchers("/user/*")
                    .authenticated()
                .antMatchers("/admin/**")
                    .hasRole("ADMIN")
                .and()
                .formLogin().permitAll();

    }
}

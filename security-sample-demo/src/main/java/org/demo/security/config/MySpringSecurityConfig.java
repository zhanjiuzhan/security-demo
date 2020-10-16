package org.demo.security.config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

/**
 * @author Administrator
 */
@Configurable
public class MySpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/welcome").permitAll()
            .antMatchers("/user/*").authenticated()
            .antMatchers("/admin/**").hasRole("ADMIN")
            .and().formLogin();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
        auth.inMemoryAuthentication()
            .withUser("root1").password("abc123").roles("USER").and()
            .withUser("root2").password("abc123").roles("ADMIN").and()
            .passwordEncoder(NoOpPasswordEncoder.getInstance());
    }
}

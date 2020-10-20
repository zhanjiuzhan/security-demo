package org.demo.security.config;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 */
@Configurable
public class MySpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 配置权限连接
        http.authorizeRequests()
                // 该链接不需要权限
                .antMatchers("/welcome").permitAll()
                // 该链接需要USER权限
                .antMatchers("/user/*").hasRole("USER")
                // 该链接需要Admin权限
                .antMatchers("/admin/**").hasRole("ADMIN")
                // 其它的需要登陆权限
                .anyRequest().authenticated()
                // 闭合权限设置
            .and()
            // 配置登陆 可以配置登陆的用户名 密码 和登陆路径 等 据有大量的默认值
            .formLogin()
                // 登陆成功的处理 也可以使用其跳转页面的方法
                .successHandler(getLoginSuccessHandler())
                // 登陆失败的处理
                .failureHandler(getLoginFailureHandler())
            // 结束登陆配置
            .and()
            // 配置用户登出 由于CSRF默认开启的原因是的登陆退出需要时POST请求 所以一般关闭CSRF
            // 默认在注销用户时 会进行1.session 失效 2.清楚SecurityContext 3.页面重定向到login?logout
            .logout()
            .and()
            // Session 管理
            .sessionManagement()
                // session 策略 注: 只表示security本使是否创建session 你也可以在应用中创建
                // 1. always 如果session 不存在总是创建
                // 2. ifRequired 仅当需要的时候创建session 默认配置
                // 3. never 从不创建session 如果有直接使用
                // 4. stateless 禁用session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .invalidSessionStrategy(getSessionInvalidHandler())
                // 设置session 数 多个的话算是并发了 开多个浏览器 只能有一个在线一定要记得实现userDetail的equals方法
                .maximumSessions(1).expiredSessionStrategy(getSessionExpiredHandler()).and()
                // session 的超时时间在server配置即可 至少60秒
            .and()
            // 配置访问异常处理
            .exceptionHandling()
                .accessDeniedHandler(getExceptionHandler())
            .and()
            // csrf默认开启 它做了一系列配置 对注销 登陆 有影响 这里暂时不启用
            .csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 当加上这句话的时候
        //   会去找实现了UserDetailService的Bean作为数据源
        //     没有的话会使用默认的UserDetailsService的Bean 即默认的用户名和密码 或者 配置文件指定的用户名密码
        //     有的话 用自己配置的UserDetailsService的Bean
        // 当不加这句 那么会用 local的配置 也就是该方法中的实现来作为用户源 比如该方法时空实现 那么会登陆报错没有AuthenticationProvider
        super.configure(auth);

        auth.inMemoryAuthentication()
            .withUser("root1").password("$2a$10$ogqSsM0uWI7m0qx0rMeX1uyTKMPsBt1CcdykTJD3RVEcbd42Z2PMq").roles("USER").and()
            .withUser("root2").password("$2a$10$ogqSsM0uWI7m0qx0rMeX1uyTKMPsBt1CcdykTJD3RVEcbd42Z2PMq").roles("ADMIN");

        // 所以：
        // 1) 配置自己的数据来源 即 实现UserDetailService 提供这样一个 Bean 就不要重写这个方法
        // 2) 使用内存来添加用户信息 那么重写这个方法 super.configure(auth); 这句不要添加
        // 3) 使用配置文件 配置一个用户信息 不要重写这个方法 其实它也是存储在内存的 缺点: 只能配置一个用户
    }

    /**
     * 密码加密解密Bean 不自己配置也会有默认的
     * @return
     */
    @Bean
    private PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private AccessDeniedHandler getExceptionHandler() {
        return (request, response, accessDeniedException) -> {
            HashMap result = new HashMap(2);
            result.put("code", 403);
            result.put("msg", "用户访问异常: " + accessDeniedException.getMessage());
            response(response, result);
        };
    }

    /**
     * 登陆成功的处理
     * @return
     */
    private AuthenticationSuccessHandler getLoginSuccessHandler() {
        return (request, response, authentication) -> {
            HashMap result = new HashMap(2);
            result.put("code", 200);
            result.put("msg", "用户: " + authentication.getName() + " 登陆成功");
            response(response, result);
        };
    }

    /**
     * 登陆失败的处理
     * @return
     */
    private AuthenticationFailureHandler getLoginFailureHandler() {
        return (request, response, exception) -> {
            HashMap result = new HashMap(2);
            result.put("code", 401);
            result.put("msg", "用户登陆失败: " + exception.getMessage());
            response(response, result);
        };
    }

    /**
     * session 失效
     * @return
     */
    private InvalidSessionStrategy getSessionInvalidHandler() {
        return (request, response) -> {
            HashMap result = new HashMap(2);
            result.put("code", 401);
            result.put("msg", "用户未登陆或者 过期 请重新登陆");
            response(response, result);
        };
    }

    /**
     * session 并发过期啥的
     * @return
     */
    private SessionInformationExpiredStrategy getSessionExpiredHandler() {
        return event -> {
            HashMap result = new HashMap(2);
            result.put("code", 500);
            result.put("msg", "用户重复登陆了");
            response(event.getResponse(), result);
        };
    }

    /**
     * 根据map 返回一个字符串 设置到请求返回体中
     * @param parameter
     * @return
     */
    private void response(HttpServletResponse response, Map<String, Object> parameter) throws IOException {
        response.setContentType("application/json; charset=utf-8");
        String msg = JSON.toJSONString(parameter);
        response.setContentLength(msg.getBytes(StandardCharsets.UTF_8).length);
        Writer out = response.getWriter();
        out.write(msg);
        out.flush();
    }

    public static void main(String[] args) {
        /**
         * BCryptPasswordEncoder 是默认的加密方式 那么这里需要知道加密后的密码是啥样子
         * 运行结果为 $2a$10$ogqSsM0uWI7m0qx0rMeX1uyTKMPsBt1CcdykTJD3RVEcbd42Z2PMq
         */
        System.out.println(new BCryptPasswordEncoder().encode("abc123"));
    }
}

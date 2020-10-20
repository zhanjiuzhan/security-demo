package org.demo.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Administrator
 */
@RestController
public class SecurityController {

    /**
     * 这个无权限
     * @return
     */
    @GetMapping("welcome")
    public String welCome() {
        return "welcome spring security demo";
    }

    /**
     * 需要用户身份是 Admin
     * @return
     */
    @GetMapping("guest/welcome")
    public String welComeGuest() {
        return "welcome spring security demo (你具有登陆权限)";
    }

    /**
     * 需要用户身份是 USER
     * @return
     */
    @GetMapping("user/welcome")
    public String welComeUser() {
        return "welcome spring security demo (你具有User的权限)";
    }

    /**
     * 需要用户身份是 Admin
     * @return
     */
    @GetMapping("admin/welcome")
    public String welComeAdmin() {
        return "welcome spring security demo (你具有Admin的权限)";
    }
}

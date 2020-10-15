package org.demo.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Administrator
 */
@RestController
public class SecurityController {

    @GetMapping("welcome")
    public String welCome() {
        return "welcome spring security demo";
    }

    @GetMapping("user/welcome")
    public String welComeUser() {
        return "welcome spring security demo user";
    }

    @GetMapping("admin/welcome")
    public String welComeAdmin() {
        return "welcome spring security demo admin";
    }
}

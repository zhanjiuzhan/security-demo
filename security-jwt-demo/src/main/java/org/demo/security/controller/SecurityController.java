package org.demo.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("jwt")
public class SecurityController {

    @GetMapping("get/user")
    public String getUser() {
        return "success get user";
    }
}

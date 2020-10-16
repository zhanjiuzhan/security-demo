package org.demo.security;

import org.demo.security.config.MySpringSecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * @author Administrator
 */
@SpringBootApplication
@Import(MySpringSecurityConfig.class)
public class SampleSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SampleSecurityApplication.class);
    }
}

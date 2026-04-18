package ru.itis.dis403.lab2_06;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class Lab206SpringSecurityJwtApplication {
	public static void main(String[] args) {

        SpringApplication.run(Lab206SpringSecurityJwtApplication.class, args);
	}
}

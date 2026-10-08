package com.oblms.authservice;

import com.oblms.authservice.entity.User;
import com.oblms.authservice.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class AuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }

    // Fixed admin: Lucky@learnmatrix.com / Lucky@123 (login only, no registration).
    @Bean
    public CommandLineRunner seedAdmin(UserRepository users, PasswordEncoder encoder) {
        return args -> {
            String adminEmail = "Lucky@learnmatrix.com";
            if (users.findByEmail(adminEmail).isEmpty()) {
                users.save(new User("Admin", adminEmail, encoder.encode("Lucky@123"), "ADMIN"));
                System.out.println("Seeded admin: " + adminEmail);
            }
        };
    }

}

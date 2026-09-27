package com.procureflow.config;

import com.procureflow.entity.Role;
import com.procureflow.entity.User;
import com.procureflow.repository.RoleRepository;
import com.procureflow.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(
            RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            // Create roles
            createRoleIfNotExists(roleRepository, "EMPLOYEE");
            createRoleIfNotExists(roleRepository, "MANAGER");
            createRoleIfNotExists(roleRepository, "PROCUREMENT");
            createRoleIfNotExists(roleRepository, "FINANCE");

            // Create test employee
            Role employeeRole = roleRepository.findByName("EMPLOYEE")
                    .orElseThrow();

            if (userRepository.findByEmail("employee@gmail.com").isEmpty()) {

                User user = new User();

                user.setName("Test Employee");
                user.setEmail("employee@gmail.com");
                user.setPassword(
                        passwordEncoder.encode("password123")
                );
                user.setRole(employeeRole);
                user.setActive(true);

                userRepository.save(user);
            }
        };
    }
    @Bean
    CommandLineRunner initUsers(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            Role employeeRole =
                    roleRepository.findByName("EMPLOYEE").orElseThrow();

            User user = userRepository
                    .findByEmail("employee@gmail.com")
                    .orElse(new User());

            user.setName("Employee");
            user.setEmail("employee@gmail.com");
            user.setPassword(passwordEncoder.encode("employee123"));
            user.setRole(employeeRole);
            user.setActive(true);

            userRepository.save(user);
        };
    }

    // Creates one demo login per role (MANAGER/PROCUREMENT/FINANCE) so the full
    // workflow can be tested end-to-end. Idempotent: safe to run on every startup.
    @Bean
    CommandLineRunner initWorkflowUsers(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {
            createUserIfNotExists(userRepository, roleRepository, passwordEncoder,
                    "manager@gmail.com", "Test Manager", "manager123", "MANAGER");

            createUserIfNotExists(userRepository, roleRepository, passwordEncoder,
                    "procurement@gmail.com", "Test Procurement", "procurement123", "PROCUREMENT");

            createUserIfNotExists(userRepository, roleRepository, passwordEncoder,
                    "finance@gmail.com", "Test Finance", "finance123", "FINANCE");
        };
    }

    private void createUserIfNotExists(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            String email,
            String name,
            String rawPassword,
            String roleName) {

        if (userRepository.findByEmail(email).isPresent()) {
            return;
        }

        Role role = roleRepository.findByName(roleName).orElseThrow();

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(role);
        user.setActive(true);

        userRepository.save(user);
    }

    private void createRoleIfNotExists(
            RoleRepository roleRepository,
            String roleName) {

        if (roleRepository.findByName(roleName).isEmpty()) {
            roleRepository.save(new Role(roleName));
        }
    }
}
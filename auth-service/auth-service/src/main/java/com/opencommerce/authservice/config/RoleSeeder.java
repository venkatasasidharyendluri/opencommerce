package com.opencommerce.authservice.config;

import com.opencommerce.authservice.entity.Role;
import com.opencommerce.authservice.enums.RoleType;
import com.opencommerce.authservice.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RoleSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String @NonNull ... args) {

        if (roleRepository.findByName(RoleType.ROLE_ADMIN).isEmpty()) {

            roleRepository.save(
                    Role.builder()
                            .name(RoleType.ROLE_ADMIN)
                            .build()
            );
        }

        if (roleRepository.findByName(RoleType.ROLE_CUSTOMER).isEmpty()) {

            roleRepository.save(
                    Role.builder()
                            .name(RoleType.ROLE_CUSTOMER)
                            .build()
            );
        }
    }
}
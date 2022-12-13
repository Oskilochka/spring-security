package com.security.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static com.security.security.config.ApplicationUserRole.ADMIN;
import static com.security.security.config.ApplicationUserRole.STUDENT;
import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
public class ApplicationSecurityConfiguration {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public  ApplicationSecurityConfiguration(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .securityMatcher(antMatcher("/api/**"))
                .authorizeHttpRequests((authz) -> authz
//                        .requestMatchers("/resources/**", "/signup", "/about").permitAll()
//                        .requestMatchers("/admin/**").hasRole("ADMIN")
                                .anyRequest()
                                .authenticated()
                )

                .httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails juliaUser = User.builder()
                .username("julia")
                .password(passwordEncoder.encode("password"))
                .roles(STUDENT.name())
                .build();

        UserDetails adminUser = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .roles(ADMIN.name())
                .build();


        return new InMemoryUserDetailsManager(
                juliaUser,
                adminUser
        );
    }
}
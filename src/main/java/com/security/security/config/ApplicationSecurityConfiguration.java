package com.security.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static com.security.security.config.ApplicationUserPermission.COURSE_WRITE;
import static com.security.security.config.ApplicationUserRole.*;
import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
public class ApplicationSecurityConfiguration {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ApplicationSecurityConfiguration(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf().disable().authorizeHttpRequests(auth -> auth
                        .requestMatchers("").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/management/api/**").hasAuthority(COURSE_WRITE.name())
                        .requestMatchers(HttpMethod.POST, "/management/api/**").hasAuthority(COURSE_WRITE.name())
                        .requestMatchers(HttpMethod.PUT, "/management/api/**").hasAuthority(COURSE_WRITE.name())
                        .requestMatchers(HttpMethod.GET, "/management/api/**").hasAnyRole(ADMIN.name(), ADMINTRAINEE.name())
                        .requestMatchers("/api/**").hasRole(ADMIN.name())
                        .anyRequest()
                        .authenticated()
                )
                .httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    protected UserDetailsService userDetailsService() {
//      USER
        UserDetails juliaUser = User.builder()
                .username("julia")
                .password(passwordEncoder.encode("password"))
                .roles(STUDENT.name())
                .build();

//        ADMIN
        UserDetails adminUser = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .roles(ADMIN.name())
                .build();

//      ADMIN Trainee
        UserDetails adminTraineeUser = User.builder()
                .username("adminTrainee")
                .password(passwordEncoder.encode("admin"))
                .roles(ADMINTRAINEE.name())
                .build();

        return new InMemoryUserDetailsManager(
                juliaUser,
                adminUser,
                adminTraineeUser
        );
    }
}
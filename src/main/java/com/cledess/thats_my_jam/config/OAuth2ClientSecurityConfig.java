package com.cledess.thats_my_jam.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class OAuth2ClientSecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .authorizeHttpRequests((var authorizeHttpRequestsCustomizer) -> {
                authorizeHttpRequestsCustomizer.requestMatchers("/token").authenticated();
                authorizeHttpRequestsCustomizer.requestMatchers("/**").permitAll();
            })
            .oauth2Login(withDefaults())
            .build();
    }
}

package com.raphaelmb.store.config;

import com.raphaelmb.store.entities.Role;
import com.raphaelmb.store.filters.JwtAuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {
    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public AuthenticationProvider authenticationProvider() {
        var provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        String prefixV1 = "/api/v1";
        http
                .sessionManagement(c -> {
                    c.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(c -> {
                    c
                            .requestMatchers(prefixV1 + "/carts/**").permitAll()
                            .requestMatchers(prefixV1 + "/admin/**").hasRole(Role.ADMIN.name())
                            .requestMatchers(HttpMethod.POST,prefixV1 + "/users").permitAll()
                            .requestMatchers(HttpMethod.GET,prefixV1 + "/products/**").permitAll()
                            .requestMatchers(HttpMethod.POST,prefixV1 + "/prodcuts/**").hasRole(Role.ADMIN.name())
                            .requestMatchers(HttpMethod.PUT,prefixV1 + "/prodcuts/**").hasRole(Role.ADMIN.name())
                            .requestMatchers(HttpMethod.DELETE,prefixV1 + "/prodcuts/**").hasRole(Role.ADMIN.name())
                            .requestMatchers(HttpMethod.POST,prefixV1 + "/auth/login").permitAll()
                            .requestMatchers(HttpMethod.POST,prefixV1 + "/auth/refresh").permitAll()
                            .requestMatchers(HttpMethod.POST,prefixV1 + "/checkout/webhook").permitAll()
                            .requestMatchers(prefixV1 + "/docs/**").permitAll()
                            .requestMatchers(prefixV1 + "/swagger-ui/**").permitAll()
                            .requestMatchers(prefixV1 + "/api-docs/**").permitAll()
                            .anyRequest().authenticated();
                })
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(c -> {
                    c.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
                    c.accessDeniedHandler(((request, response, accessDeniedException) ->
                            response.setStatus(HttpStatus.FORBIDDEN.value()))
                    );
                });

        return http.build();
    }
}
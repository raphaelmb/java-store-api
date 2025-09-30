package com.raphaelmb.store.common;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

@Component
public class SwaggerSecurityRules implements SecurityRules {
    @Override
    public void configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
        registry.requestMatchers(prefixV1 + "/docs/**").permitAll()
                .requestMatchers(prefixV1 + "/swagger-ui/**").permitAll()
                .requestMatchers(prefixV1 + "/api-docs/**").permitAll();
    }
}

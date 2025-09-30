package com.raphaelmb.store.products;

import com.raphaelmb.store.common.SecurityRules;
import com.raphaelmb.store.users.Role;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

@Component
public class ProductSecurityRules implements SecurityRules {
    @Override
    public void configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
        registry.requestMatchers(HttpMethod.GET,prefixV1 + "/products/**").permitAll()
                .requestMatchers(HttpMethod.POST,prefixV1 + "/products/**").hasRole(Role.ADMIN.name())
                .requestMatchers(HttpMethod.PUT,prefixV1 + "/products/**").hasRole(Role.ADMIN.name())
                .requestMatchers(HttpMethod.DELETE,prefixV1 + "/products/**").hasRole(Role.ADMIN.name());
    }
}

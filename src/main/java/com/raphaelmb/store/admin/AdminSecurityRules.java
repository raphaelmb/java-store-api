package com.raphaelmb.store.admin;

import com.raphaelmb.store.common.SecurityRules;
import com.raphaelmb.store.users.Role;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

@Component
public class AdminSecurityRules implements SecurityRules {
    @Override
    public void configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
        registry.requestMatchers(prefixV1 + "/admin/**").hasRole(Role.ADMIN.name());
    }
}

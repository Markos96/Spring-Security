package com.curso.springsecurity.config.security;

import com.curso.springsecurity.config.security.filter.JwtAuthenticationFilter;
import com.curso.springsecurity.utils.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * There are 2 ways to configure security:
     *
     * The buildRequestMatcher method uses the configuration by routes while commenting the method
     * can be used by @PreAuthorize and @PostAuthorize annotations.
     *
     * @param http
     * @param authenticationProvider
     * @return
     * @throws Exception
     */

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationProvider authenticationProvider) throws Exception {
        http
                .csrf(csrfConfig -> csrfConfig.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
                //.authorizeHttpRequests(buildRequestMatchers());


        return http.build();
    }

    private static Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> buildRequestMatchers() {
        return authorize -> {
            authorize.requestMatchers(HttpMethod.POST, "auth/authenticate").permitAll();
            authorize.requestMatchers(HttpMethod.GET, "auth/public-access").permitAll();
            authorize.requestMatchers("/error").permitAll();
            authorize.requestMatchers(HttpMethod.GET, "/products").hasAuthority(Permission.READ_ALL_PRODUCTS.name());
            authorize.requestMatchers(HttpMethod.POST, "/products").hasAuthority(Permission.SAVE_ONE_PRODUCT.name());
            authorize.anyRequest().denyAll();
        };
    }
}

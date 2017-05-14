package com.example.ekanban.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
/*        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("*//**").permitAll();*/

        http
                .cors()
            .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .requestMatchers().antMatchers("/**","/api/**")
            .and()
                .authorizeRequests()
//                    .antMatchers("/api/users","/api/users/**").access("hasRole('ADMIN')")
//                    .antMatchers("/api/categories","/api/categories/**").access("hasRole('ADMIN')")
                    .antMatchers(HttpMethod.GET,"/api/categories").permitAll()
                    .antMatchers(HttpMethod.GET,"/api/sections").permitAll()
                    .antMatchers(HttpMethod.GET,"/api/suppliers").permitAll()
                    .antMatchers(HttpMethod.GET,"/api/users").permitAll()
                    .antMatchers(HttpMethod.GET,"/api/inventory").permitAll()
                    .antMatchers(HttpMethod.GET,"/api/orders").permitAll()
                    .antMatchers(HttpMethod.GET,"/api/products/**").permitAll()
                    .antMatchers("/api/**").authenticated()
                    .antMatchers("/**").permitAll()

            .and()
                .exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());

    }
}

//        http.
//        anonymous().disable()
//        .requestMatchers().antMatchers("/**")
//        .and().authorizeRequests()
//        .antMatchers("/**").access("hasRole('ADMIN')")
//        .and().exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
// @formatter:off
//        http
//                // Since we want the protected resources to be accessible in the UI as well we need 
//                // session creation to be allowed (it's disabled by default in 2.0.6)
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
//                .and()
//                .requestMatchers().antMatchers("/photos/**", "/oauth/users/**", "/oauth/clients/**", "/me")
//                .and()
//                .authorizeRequests()
//                .antMatchers("/me").access("#oauth2.hasScope('read')")
//                .antMatchers("/photos").access("#oauth2.hasScope('read') or (!#oauth2.isOAuth() and hasRole('ROLE_USER'))")
//                .antMatchers("/photos/trusted/**").access("#oauth2.hasScope('trust')")
//                .antMatchers("/photos/user/**").access("#oauth2.hasScope('trust')")
//                .antMatchers("/photos/**").access("#oauth2.hasScope('read') or (!#oauth2.isOAuth() and hasRole('ROLE_USER'))")
//                .regexMatchers(HttpMethod.DELETE, "/oauth/users/([^/].*?)/tokens/.*")
//                .access("#oauth2.clientHasRole('ROLE_CLIENT') and (hasRole('ROLE_USER') or #oauth2.isClient()) and #oauth2.hasScope('write')")
//                .regexMatchers(HttpMethod.GET, "/oauth/clients/([^/].*?)/users/.*")
//                .access("#oauth2.clientHasRole('ROLE_CLIENT') and (hasRole('ROLE_USER') or #oauth2.isClient()) and #oauth2.hasScope('read')")
//                .regexMatchers(HttpMethod.GET, "/oauth/clients/.*")
//                .access("#oauth2.clientHasRole('ROLE_CLIENT') and #oauth2.isClient() and #oauth2.hasScope('read')");
        // @formatter:on

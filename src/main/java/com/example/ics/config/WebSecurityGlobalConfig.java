package com.example.ics.config;

import com.example.ics.dto.UserDto;
import com.example.ics.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class WebSecurityGlobalConfig extends GlobalAuthenticationConfigurerAdapter {

    @Autowired
    UserService userService;

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Bean
    protected UserDetailsService userDetailsService() {
        return (email) -> {
            UserDto user;
            if(email.contains("@")){
                user = userService.findByEmail(email);
            }else{
                user = userService.findByUsername(email);
            }
            if (user != null) {
                return new User(user.getName(), user.getPassword(), AuthorityUtils.createAuthorityList(user.getRole()));
            } else {
                throw new UsernameNotFoundException("Could not find the user '" + email + "'");
            }
        };
    }
}

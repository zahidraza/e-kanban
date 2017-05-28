package com.example.ekanban;

import com.example.ekanban.dto.UserDto;
import com.example.ekanban.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.example.ekanban.storage.BarcodeService;
import com.example.ekanban.util.ConfigUtil;
import com.example.ekanban.util.Constants;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@SpringBootApplication
@Controller
public class Application extends SpringBootServletInitializer {

    private final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    @Bean
    @Profile(value = { "production", "default" })
    CommandLineRunner init(
            BarcodeService barcodeService,
            UserService userService) {

        return (args) -> {
            barcodeService.init();
            if (userService.count() == 0) {
                Properties props = ConfigUtil.getConfigProperties();
                String user = props.getProperty(Constants.ADMIN_USER_NAME);
                String email = props.getProperty(Constants.ADMIN_USER_EMAIL);
                String mobile = props.getProperty(Constants.ADMIN_USER_MOBILE);
                String appUser = props.getProperty(Constants.APP_USER_NAME);
                String appEmail = props.getProperty(Constants.APP_USER_EMAIL);
                String appMobile = props.getProperty(Constants.APP_USER_MOBILE);

                logger.info("Initializing root user: username = {}, email = {} , mobile = {}, role = {}",user,email,mobile, "ADMIN");
                logger.info("Initializing app user: username = {}, email = {} , mobile = {}, role = {}",appUser,appEmail,appMobile,"PURCHASE");

                userService.save(new UserDto(appUser, appEmail, "PURCHASE", appMobile,true));
                userService.save(new UserDto(user, email, "ADMIN", mobile,true));
            }
        };
    }

    @Bean
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }

    @Bean
    public Mapper dozerBeanMapper() {
        List<String> list = new ArrayList<>();
        list.add("dozer_mapping.xml");
        return new DozerBeanMapper(list);
    }

    @GetMapping(value = "/")
    public String hello() {
        logger.debug("home page");
        return "index";
    }

}

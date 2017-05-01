package com.example.ekanban.page.converter;

import com.example.ekanban.dto.UserDto;
import com.example.ekanban.entity.User;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserConverter implements Converter<User, UserDto>{

    @Autowired Mapper mapper;
    
    @Override
    public UserDto convert(User source) {
        return mapper.map(source, UserDto.class);
    }

    
}

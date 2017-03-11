package com.example.spring.oauth2.dozer.converter;

import com.example.spring.oauth2.entity.Role;
import org.dozer.DozerConverter;

public class RoleConverter extends DozerConverter<String, Role>{

    public RoleConverter() {
        super(String.class, Role.class);
    }
    
    @Override
    public Role convertTo(String source, Role destination) {
        if(source == null) return null;
        return Role.parse(source);
    }

    @Override
    public String convertFrom(Role source, String destination) {
        if(source == null) return null;
        return source.getValue();
    }
    
}

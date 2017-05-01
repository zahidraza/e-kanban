package com.example.ekanban.dozer.converter;

import com.example.ekanban.enums.Role;
import org.dozer.DozerConverter;

public class StringRoleConverter extends DozerConverter<String, Role>{

    public StringRoleConverter() {
        super(String.class, Role.class);
    }
    
    @Override
    public Role convertTo(String source, Role destination) {
        if(source == null) return null;
        return Role.parse("ROLE_"+source);
    }

    @Override
    public String convertFrom(Role source, String destination) {
        if(source == null) return null;
        return source.name();
    }
    
}

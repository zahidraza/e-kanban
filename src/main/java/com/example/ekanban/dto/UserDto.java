package com.example.ekanban.dto;

import com.example.ekanban.enums.Role;
import com.example.ekanban.enums.StringEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserDto {

    private Long id;

    @NotNull
    @Size(min = 5, max = 100)
    private String name;

    @NotNull
    @Pattern(regexp=".+@.+\\..+")
    private String email;

    @JsonIgnore
    private String password;

    @NotNull
    @StringEnum(enumClass = Role.class)
    private String role;

    @NotNull
    @Pattern(regexp="[0-9]{10}")
    private String mobile;

    public UserDto() {
    }

    public UserDto(String name, String email, String role, String mobile) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.mobile = mobile;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    
    public String toJsonString(){
        StringBuilder builder = new StringBuilder();
        builder.append("{\n");
        if(id != null) builder.append("\"id\":" + id + ",\n");
        if(name != null) builder.append("\"name\":\"" + name + "\",\n");
        if(email != null) builder.append("\"email\":\"" + email + "\",\n");
        if(role != null) builder.append("\"role\":\"" + role + "\",\n");
        if(mobile != null) builder.append("\"mobile\":\"" + mobile + "\",\n");
        
        if(builder.length() > 2){
            builder.setLength(builder.length()-2);
        }
        builder.append("\n}");
        return builder.toString();
    }

    @Override
    public String toString() {
        return "UserDto{" + "id=" + id + ", name=" + name + ", email=" + email + ", role=" + role + ", mobile=" + mobile + '}';
    }
}

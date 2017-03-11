package com.example.spring.oauth2.respository;

import com.example.spring.oauth2.entity.User;
import java.io.Serializable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRespository extends JpaRepository<User, Long>{
    
    public User findByEmail(String email);
    
    public User findByName(String name);
}

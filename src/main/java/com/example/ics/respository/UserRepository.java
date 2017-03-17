package com.example.ics.respository;

import com.example.ics.entity.User;
import java.io.Serializable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>{
    
    public User findByEmail(String email);
    
    public User findByName(String name);
}

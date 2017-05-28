package com.example.ekanban.respository;

import com.example.ekanban.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>{
    
    public User findByEmailIgnoreCase(String email);
    
    public User findByNameIgnoreCase(String name);
}

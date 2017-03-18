package com.example.ics.service;

import com.example.ics.respository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.junit.Before;
import org.mockito.Mockito;

public class UserServiceTest {
    private UserService userService;
    private UserRepository userRepository;
    private Mapper mapper;
    
    //@Before
    public void setUp(){
        this.userRepository = Mockito.mock(UserRepository.class);
        List<String> list = new ArrayList<>();
        list.add("dozer_mapping.xml");
        this.mapper =  new DozerBeanMapper(list);
        this.userService = new UserService(userRepository, mapper);
    }
    
    
}

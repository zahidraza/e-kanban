package com.example.ekanban.service;

import com.example.ekanban.dto.UserDto;
import com.example.ekanban.entity.User;
import com.example.ekanban.enums.Role;
import com.example.ekanban.respository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import org.dozer.Mapper;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.BDDMockito.*;
import org.mockito.Mockito;

//@Ignore
public class UserServiceTest {
    private UserService userService;
    private UserRepository userRepository;
    private Mapper mapper;
    
    private List<User> userList = new ArrayList<>();
    private List<UserDto> userDtoList = new ArrayList<>();
    
    @Before
    public void setUp(){
        this.userRepository = Mockito.mock(UserRepository.class);
        this.mapper = Mockito.mock(Mapper.class);
        this.userService = new UserService(userRepository, mapper);
        userList.add(new User("Md Zahid Raza", "zahid7292@gmail.com", "admin", Role.ADMIN.getValue(), "8987525008"));
        userDtoList.add(new UserDto("Md Zahid Raza", "zahid7292@gmail.com", Role.ADMIN.name(), "8987525008",true));
    }
    
    @Test
    public void findOneSuccess(){
        given(this.userRepository.findOne(anyLong())).willReturn(userList.get(0));
        given(this.mapper.map(userList.get(0), UserDto.class)).willReturn(userDtoList.get(0));
        
        assertNotNull(this.userService.findOne(1L));
        
        verify(userRepository,atLeastOnce()).findOne(anyLong());
        verify(mapper,atLeastOnce()).map(userList.get(0), UserDto.class);
    }
    
    @Test
    public void findOneFail(){
        given(this.userRepository.findOne(anyLong())).willReturn(null);       
        assertNull(this.userService.findOne(1L));        
        verify(userRepository,atLeastOnce()).findOne(anyLong());
    }
    
}

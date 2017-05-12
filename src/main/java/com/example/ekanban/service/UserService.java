package com.example.ekanban.service;

import com.example.ekanban.dto.UserDto;
import com.example.ekanban.entity.User;
import com.example.ekanban.enums.Role;
import com.example.ekanban.page.converter.UserConverter;
import java.util.List;
import java.util.stream.Collectors;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.ekanban.respository.UserRepository;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final Mapper mapper;
    private UserConverter converter;

    @Autowired
    public UserService(UserRepository userRepository, Mapper mapper){
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Autowired
    public void setConverter(UserConverter converter) {
        this.converter = converter;
    }
///////////////////////////////////////////////////////////////////////////////////
    public UserDto findOne(Long id) {
        logger.debug("findOne(): id = {}",id);
        User user = userRepository.findOne(id);
        if(user == null) return null;
        return mapper.map(user, UserDto.class);
    }

    public List<UserDto> findAll() {
        logger.debug("findAll()");
        return userRepository.findAll().stream()
                .map(user -> mapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }
    
    public Page<UserDto> findAllByPage(Pageable pageable){
        logger.debug("findAllByPage()");
        return userRepository.findAll(pageable).map(converter);
    }

    public UserDto findByEmail(String email) {
        logger.debug("findByEmail(): email = {}",email);
        User user = userRepository.findByEmail(email);
        if(user == null) return null;
        return mapper.map(user, UserDto.class);
    }
    
    public UserDto findByUsername(String username) {
        logger.debug("findByUsername(): name = " , username);
        User user = userRepository.findByName(username);
        if(user == null) return null;
        return mapper.map(user, UserDto.class);
    }

    public Boolean exists(Long id) {
        logger.debug("exists(): id = ",id);
        return userRepository.exists(id);
    }
    
    public Long count(){
        logger.debug("count()");
        return userRepository.count();
    }

    @Transactional
    public UserDto save(UserDto userDto) {
        logger.debug("save()");
        User user = mapper.map(userDto, User.class);
        user.setPassword(userDto.getMobile());
        user.setActive(true);
        user = userRepository.save(user);
        return mapper.map(user, UserDto.class);
    }

    @Transactional
    public UserDto update(UserDto userDto) {
        logger.debug("update()");
        User user = userRepository.findOne(userDto.getId());
        if(userDto.getName() != null)   user.setName(userDto.getName());
        if(userDto.getEmail() != null)   user.setEmail(userDto.getEmail());
        if(userDto.getMobile() != null)   user.setMobile(userDto.getMobile());
        if(userDto.getRole() != null)   user.setRole(Role.parse("ROLE_" + userDto.getRole()));
        return mapper.map(user, UserDto.class);
    }

    @Transactional
    public boolean changePassword(String email,String oldPassword,String newPassword){
        User user = userRepository.findByEmail(email);
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        if (encoder.matches(oldPassword, user.getPassword())) {
            user.setPassword(newPassword);
            return true;
        }
        return false;
    }
    
    @Transactional
    public void delete(Long id) {
        logger.debug("delete(): id = {}",id);
        userRepository.delete(id);
    }
    
}

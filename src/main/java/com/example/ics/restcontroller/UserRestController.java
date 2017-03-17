package com.example.ics.restcontroller;

import com.example.ics.assembler.UserAssembler;
import com.example.ics.dto.UserDto;
import com.example.ics.service.UserService;
import com.example.ics.util.ApiUrls;
import java.net.URI;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping(value = ApiUrls.ROOT_URL_USERS)
public class UserRestController{   
    private final Logger logger = LoggerFactory.getLogger(UserRestController.class);
    
    private final UserService userService;
    private final UserAssembler userAssembler;

    @Autowired
    public UserRestController(UserService userService, UserAssembler userAssembler) {
        this.userService = userService;
        this.userAssembler = userAssembler;
    }
    
    @GetMapping
    public ResponseEntity<?> listAllUsers(Pageable pageable, PagedResourcesAssembler assembler) {
        logger.debug("listAllUsers()");
        Page<UserDto> page = userService.findAllByPage(pageable);
        return new ResponseEntity<>(assembler.toResource(page, userAssembler), HttpStatus.OK);
    }
  
    @GetMapping(value = ApiUrls.URL_USERS_USER)
    public ResponseEntity<?> getUser(@PathVariable("userId") long userId) {
        logger.debug("getUser(): userId = {}",userId);
        UserDto user = userService.findOne(userId);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userAssembler.toResource(user), HttpStatus.OK);
    }
   
    @PostMapping
    public ResponseEntity<Void> createUser(@Valid @RequestBody UserDto user) {
        logger.debug("createUser():\n {}", user.toString());
        user = userService.save(user);
        Link selfLink = linkTo(UserRestController.class).slash(user.getId()).withSelfRel();
        return ResponseEntity.created(URI.create(selfLink.getHref())).build();
    }
 
    @PutMapping(value = ApiUrls.URL_USERS_USER)
    public ResponseEntity<?> updateUser(@PathVariable("userId") long userId,@Valid @RequestBody UserDto user) {
        logger.debug("updateUser(): userId = {} \n {}",userId,user);
        if (!userService.exists(userId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        user.setId(userId);  
        user = userService.update(user);
        return new ResponseEntity<>(userAssembler.toResource(user), HttpStatus.OK);
    }
  
    @DeleteMapping(value = ApiUrls.URL_USERS_USER)
    public ResponseEntity<Void> deleteUser(@PathVariable("userId") long userId) {
        logger.debug("deleteUser(): id = {}",userId);
        if (!userService.exists(userId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userService.delete(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}

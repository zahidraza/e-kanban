package com.example.ics.restcontroller;

import com.example.ics.dto.UserDto;
import org.codehaus.jackson.map.ObjectMapper;
import static org.hamcrest.Matchers.is;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
//@Ignore
public class UserIntegrationTest {
    
    @Autowired MockMvc mvc;
    private final String contentType = "application/hal+json;charset=UTF-8";
    
    //@Before
    public void setUp(){
        ObjectMapper mapper = new ObjectMapper();
    }
    
    @Test
    public void getAllUsers() throws Exception{
        this.mvc.perform(get("/api/users?sort=id,asc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$._embedded.userDtoList",hasSize(3)))
                .andExpect(jsonPath("$._embedded.userDtoList[1].name",is("Md Jawed Akhtar")));
    }
    
    @Test
    public void getAllUsersPage() throws Exception{
        this.mvc.perform(get("/api/users?page=1&size=1&sort=id,asc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$._embedded.userDtoList",hasSize(1)))
                .andExpect(jsonPath("$._embedded.userDtoList[0].name",is("Md Jawed Akhtar")))
                .andExpect(jsonPath("$._links.first").exists())
                .andExpect(jsonPath("$._links.prev").exists())
                .andExpect(jsonPath("$._links.self").exists())
                .andExpect(jsonPath("$._links.next").exists())
                .andExpect(jsonPath("$._links.last").exists())
                .andExpect(jsonPath("$.page").exists());
    }
    
    @Test
    public void getUser() throws Exception{
        this.mvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.name", is("Md Zahid Raza")))
                .andExpect(jsonPath("$._links.self").exists());
        
        this.mvc.perform(get("/api/users/10"))
                .andExpect(status().isNotFound());
    }
    
    
    @Test
    public void createAndDeleteUser() throws Exception{
        UserDto user = new UserDto("Test User", "test@gmail.com", "USER", "8987525008");
        MvcResult mvcResult = mvc.perform(post("/api/users")
                .content(user.toJsonString())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated())
                .andReturn();
        String locationUri = mvcResult.getResponse().getHeader("Location");
        assertTrue(locationUri.contains("/api/users"));
        
        int idx = locationUri.lastIndexOf('/');
        String id = locationUri.substring(idx+1);
        
        this.mvc.perform(get("/api/users/{id}",id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.name", is("Test User")));
        
        this.mvc.perform(delete("/api/users/{id}", id))
                .andExpect(status().isNoContent());

        this.mvc.perform(get("/api/users/{id}", id))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void createUserBadRequest() throws Exception{
        UserDto user = new UserDto();
        this.mvc.perform(post("/api/users")
                .content(user.toJsonString())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", hasSize(4)));

        //Test each fields one by one
        user = new UserDto("", "test@gmail.com", "ADMIN", "8987525008");
        this.mvc.perform(post("/api/users")
                .content(user.toJsonString())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].field", is("name")))
                .andExpect(jsonPath("$[0].message", containsString("length must be between 5")));
        
        user = new UserDto("Md Zahid Raza", "test", "ADMIN", "8987525008");
        this.mvc.perform(post("/api/users")
                .content(user.toJsonString())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].field", is("email")))
                .andExpect(jsonPath("$[0].message", containsString("Incorrect email")));
        
        user = new UserDto("Md Zahid Raza", "test@gmail.com", "ADMINISTARTOR", "8987525008");
        this.mvc.perform(post("/api/users")
                .content(user.toJsonString())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].field", is("role")))
                .andExpect(jsonPath("$[0].message", containsString("Accepted values are [ADMIN,USER")));
        
        user = new UserDto("Md Zahid Raza", "test@gmail.com", "ADMIN", "8987525");
        this.mvc.perform(post("/api/users")
                .content(user.toJsonString())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].field", is("mobile")))
                .andExpect(jsonPath("$[0].message", containsString("mobile number should be 10 digit long")));

    }

}

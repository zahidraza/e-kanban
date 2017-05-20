package com.example.ekanban.restcontroller;

import com.example.ekanban.entity.Section;
import com.example.ekanban.util.ApiUrls;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Ignore
public class SectionIntegrationTest {

    @Autowired MockMvc mvc;
    private final String contentType = "application/hal+json;charset=UTF-8";

    //@Test
    public void getAllSections() throws Exception{
        mvc.perform(get(ApiUrls.ROOT_URL_SECTIONS + "?sort=id,asc"))
           .andExpect(status().isOk())
           .andExpect(content().contentType(contentType))
           .andExpect(jsonPath("$._embedded.sectionList",hasSize(3)))
           .andExpect(jsonPath("$._embedded.sectionList[0].name",is("Test Section 1")))
           .andExpect(jsonPath("$._embedded.sectionList[0]._links.self").exists());
    }

    //@Test
    public void getAllSectionsPage() throws Exception{
        mvc.perform(get(ApiUrls.ROOT_URL_SECTIONS + "?page=1&size=1&sort=id,asc"))
           .andExpect(status().isOk())
           .andExpect(content().contentType(contentType))
           .andExpect(jsonPath("$._embedded.sectionList",hasSize(1)))
           .andExpect(jsonPath("$._embedded.sectionList[0].name",is("Test Section 2")))
           .andExpect(jsonPath("$._links.first").exists())
           .andExpect(jsonPath("$._links.prev").exists())
           .andExpect(jsonPath("$._links.self").exists())
           .andExpect(jsonPath("$._links.next").exists())
           .andExpect(jsonPath("$._links.last").exists())
           .andExpect(jsonPath("$.page").exists());
    }

    @Test
    public void getSection() throws Exception{
        mvc.perform(get(ApiUrls.ROOT_URL_SECTIONS + "/1"))
           .andExpect(status().isOk())
           .andExpect(content().contentType(contentType))
           .andExpect(jsonPath("$.name", is("Test Section 1")))
           .andExpect(jsonPath("$._links.self").exists());

        mvc.perform(get(ApiUrls.ROOT_URL_SECTIONS + "/10"))
           .andExpect(status().isNotFound());
    }

    @Test
    public void createAndDeleteSection() throws Exception{
        Section section = new Section("Test Section 4");
        MvcResult mvcResult = mvc.perform(post(ApiUrls.ROOT_URL_SECTIONS)
                                                  .content(section.toJsonString())
                                                  .contentType(MediaType.APPLICATION_JSON_UTF8))
                                 .andExpect(status().isCreated())
                                 .andReturn();
        String locationUri = mvcResult.getResponse().getHeader("Location");
        assertTrue(locationUri.contains(ApiUrls.ROOT_URL_SECTIONS));

        int idx = locationUri.lastIndexOf('/');
        String id = locationUri.substring(idx+1);

        mvc.perform(get(ApiUrls.ROOT_URL_SECTIONS + "/{id}",id))
           .andExpect(status().isOk())
           .andExpect(content().contentType(contentType))
           .andExpect(jsonPath("$.name", is("Test Section 4")));

        mvc.perform(delete(ApiUrls.ROOT_URL_SECTIONS + "/{id}", id))
           .andExpect(status().isNoContent());

        mvc.perform(get(ApiUrls.ROOT_URL_SECTIONS + "/{id}", id))
           .andExpect(status().isNotFound());
    }

    @Test
    public void createSectionBadRequest() throws Exception{
        Section section = new Section();
        mvc.perform(post(ApiUrls.ROOT_URL_SECTIONS)
                            .content(section.toJsonString())
                            .contentType(MediaType.APPLICATION_JSON_UTF8))
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath("$", hasSize(1)));

        //Test each fields one by one
        section = new Section("");
        mvc.perform(post(ApiUrls.ROOT_URL_SECTIONS)
                            .content(section.toJsonString())
                            .contentType(MediaType.APPLICATION_JSON_UTF8))
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath("$", hasSize(1)))
           .andExpect(jsonPath("$[0].field", is("name")))
           .andExpect(jsonPath("$[0].message", containsString("cannot be empty")));

    }

}

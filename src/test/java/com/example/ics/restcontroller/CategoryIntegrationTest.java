package com.example.ics.restcontroller;

import com.example.ics.entity.Category;
import com.example.ics.entity.SubCategory;
import com.example.ics.util.ApiUrls;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
//@Ignore
public class CategoryIntegrationTest {
    
    @Autowired MockMvc mvc;
    private final String contentType = "application/hal+json;charset=UTF-8";
    
    /////////////////////////Category API//////////////////////////////////////////////
    @Test
    public void getAllCategories() throws Exception{
        this.mvc.perform(get(ApiUrls.ROOT_URL_CATEGORIES + "?sort=id,asc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$._embedded.categoryList",hasSize(3)))
                .andExpect(jsonPath("$._embedded.categoryList[0].name",is("Test Category 1")))
                .andExpect(jsonPath("$._embedded.categoryList[0]._links.self").exists())
                .andExpect(jsonPath("$._embedded.categoryList[0]._links.subCategoryList").exists());
    }
    
    @Test
    public void getAllCategoriesPage() throws Exception{
        this.mvc.perform(get(ApiUrls.ROOT_URL_CATEGORIES + "?page=1&size=1&sort=id,asc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$._embedded.categoryList",hasSize(1)))
                .andExpect(jsonPath("$._embedded.categoryList[0].name",is("Test Category 2")))
                .andExpect(jsonPath("$._links.first").exists())
                .andExpect(jsonPath("$._links.prev").exists())
                .andExpect(jsonPath("$._links.self").exists())
                .andExpect(jsonPath("$._links.next").exists())
                .andExpect(jsonPath("$._links.last").exists())
                .andExpect(jsonPath("$.page").exists());
    }
    
    @Test
    public void getCategory() throws Exception{
        this.mvc.perform(get(ApiUrls.ROOT_URL_CATEGORIES + "/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.name", is("Test Category 1")))
                .andExpect(jsonPath("$._links.self").exists())
                .andExpect(jsonPath("$._links.subCategoryList").exists());
        
        this.mvc.perform(get(ApiUrls.ROOT_URL_CATEGORIES + "/10"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void createAndDeleteCategory() throws Exception{
        Category category = new Category("Test Category 4");
        MvcResult mvcResult = mvc.perform(post(ApiUrls.ROOT_URL_CATEGORIES)
                .content(category.toJsonString())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated())
                .andReturn();
        String locationUri = mvcResult.getResponse().getHeader("Location");
        assertTrue(locationUri.contains(ApiUrls.ROOT_URL_CATEGORIES));
        
        int idx = locationUri.lastIndexOf('/');
        String id = locationUri.substring(idx+1);
        
        this.mvc.perform(get(ApiUrls.ROOT_URL_CATEGORIES + "/{id}",id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.name", is("Test Category 4")));
        
        this.mvc.perform(delete(ApiUrls.ROOT_URL_CATEGORIES + "/{id}", id))
                .andExpect(status().isNoContent());

        this.mvc.perform(get(ApiUrls.ROOT_URL_CATEGORIES + "/{id}", id))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void createCategoryBadRequest() throws Exception{
        Category category = new Category();
        this.mvc.perform(post(ApiUrls.ROOT_URL_CATEGORIES)
                .content(category.toJsonString())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", hasSize(1)));

        //Test each fields one by one
        category = new Category("");
        this.mvc.perform(post(ApiUrls.ROOT_URL_CATEGORIES)
                .content(category.toJsonString())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].field", is("name")))
                .andExpect(jsonPath("$[0].message", containsString("empty")));
        
    }
    
    ///////////////////////////Sub Category API/////////////////////////////////////////////
    @Test
    public void getAllCategorySubCategories() throws Exception{
        this.mvc.perform(get(ApiUrls.ROOT_URL_CATEGORIES + ApiUrls.URL_CATEGORIES_CATEGORY_SUBCATEGORIES + "?sort=id,asc",1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$._embedded.subCategoryList",hasSize(3)))
                .andExpect(jsonPath("$._embedded.subCategoryList[0].name",is("Test Sub Category 1")))
                .andExpect(jsonPath("$._embedded.subCategoryList[0]._links.self").exists())
                .andExpect(jsonPath("$._embedded.subCategoryList[0]._links.productList").exists());
        
        this.mvc.perform(get(ApiUrls.ROOT_URL_CATEGORIES + ApiUrls.URL_CATEGORIES_CATEGORY_SUBCATEGORIES,10))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void getAllCategorySubCategoriesPage() throws Exception{
        this.mvc.perform(get(ApiUrls.ROOT_URL_CATEGORIES + ApiUrls.URL_CATEGORIES_CATEGORY_SUBCATEGORIES + "?page=1&size=1&sort=id,asc",1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$._embedded.subCategoryList",hasSize(1)))
                .andExpect(jsonPath("$._embedded.subCategoryList[0].name",is("Test Sub Category 2")))
                .andExpect(jsonPath("$._links.first").exists())
                .andExpect(jsonPath("$._links.prev").exists())
                .andExpect(jsonPath("$._links.self").exists())
                .andExpect(jsonPath("$._links.next").exists())
                .andExpect(jsonPath("$._links.last").exists())
                .andExpect(jsonPath("$.page").exists());
    }
    
    @Test
    public void getCategorySubCategory() throws Exception{
        this.mvc.perform(get(ApiUrls.ROOT_URL_CATEGORIES + ApiUrls.URL_CATEGORIES_CATEGORY_SUBCATEGORIES + "/1", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.name", is("Test Sub Category 1")))
                .andExpect(jsonPath("$._links.self").exists())
                .andExpect(jsonPath("$._links.productList").exists());
        
        this.mvc.perform(get(ApiUrls.ROOT_URL_CATEGORIES + ApiUrls.URL_CATEGORIES_CATEGORY_SUBCATEGORIES + "/10", 1))
                .andExpect(status().isNotFound());
        
        this.mvc.perform(get(ApiUrls.ROOT_URL_CATEGORIES + ApiUrls.URL_CATEGORIES_CATEGORY_SUBCATEGORIES,10))
                .andExpect(status().isNotFound());
    }
    
    
    @Test
    public void createAndDeleteCategorySubCategory() throws Exception{
        SubCategory subCategory = new SubCategory("Test Sub Category 4");
        System.out.println(subCategory.toJsonString());
        MvcResult mvcResult = mvc.perform(post(ApiUrls.ROOT_URL_CATEGORIES + ApiUrls.URL_CATEGORIES_CATEGORY_SUBCATEGORIES, 1)
                .content(subCategory.toJsonString())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated())
                .andReturn();
        String locationUri = mvcResult.getResponse().getHeader("Location");
        assertTrue(locationUri.contains(ApiUrls.ROOT_URL_CATEGORIES + "/1/subCategories"));
        
        int idx = locationUri.lastIndexOf('/');
        String id = locationUri.substring(idx+1);
        
        this.mvc.perform(get(ApiUrls.ROOT_URL_CATEGORIES + ApiUrls.URL_CATEGORIES_CATEGORY_SUBCATEGORIES + "/{id}", 1,id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.name", is("Test Sub Category 4")));
        
        this.mvc.perform(delete(ApiUrls.ROOT_URL_CATEGORIES + ApiUrls.URL_CATEGORIES_CATEGORY_SUBCATEGORIES + "/{id}", 1,id))
                .andExpect(status().isNoContent());

        this.mvc.perform(get(ApiUrls.ROOT_URL_CATEGORIES + ApiUrls.URL_CATEGORIES_CATEGORY_SUBCATEGORIES + "/{id}", 1,id))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void createCategorySubCategoryBadRequest() throws Exception{
        SubCategory subCategory = new SubCategory();
        this.mvc.perform(post(ApiUrls.ROOT_URL_CATEGORIES + ApiUrls.URL_CATEGORIES_CATEGORY_SUBCATEGORIES, 1)
                .content(subCategory.toJsonString())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", hasSize(1)));

        //Test each fields one by one
        subCategory = new SubCategory("");
        this.mvc.perform(post(ApiUrls.ROOT_URL_CATEGORIES + ApiUrls.URL_CATEGORIES_CATEGORY_SUBCATEGORIES, 1)
                .content(subCategory.toJsonString())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].field", is("name")))
                .andExpect(jsonPath("$[0].message", containsString("empty")));
        
    }

}


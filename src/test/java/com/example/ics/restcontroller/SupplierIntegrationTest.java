package com.example.ics.restcontroller;

import com.example.ics.entity.Supplier;
import com.example.ics.util.ApiUrls;
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
public class SupplierIntegrationTest {

    @Autowired MockMvc mvc;
    private final String contentType = "application/hal+json;charset=UTF-8";

    @Test
    public void getAllSuppliers() throws Exception{
        mvc.perform(get(ApiUrls.ROOT_URL_SUPPLIERS + "?sort=id,asc"))
           .andExpect(status().isOk())
           .andExpect(content().contentType(contentType))
           .andExpect(jsonPath("$._embedded.supplierList",hasSize(3)))
           .andExpect(jsonPath("$._embedded.supplierList[0].name",is("Test Supplier 1")))
           .andExpect(jsonPath("$._embedded.supplierList[0]._links.self").exists());
    }

    @Test
    public void getAllSuppliersPage() throws Exception{
        mvc.perform(get(ApiUrls.ROOT_URL_SUPPLIERS + "?page=1&size=1&sort=id,asc"))
           .andExpect(status().isOk())
           .andExpect(content().contentType(contentType))
           .andExpect(jsonPath("$._embedded.supplierList",hasSize(1)))
           .andExpect(jsonPath("$._embedded.supplierList[0].name",is("Test Supplier 2")))
           .andExpect(jsonPath("$._links.first").exists())
           .andExpect(jsonPath("$._links.prev").exists())
           .andExpect(jsonPath("$._links.self").exists())
           .andExpect(jsonPath("$._links.next").exists())
           .andExpect(jsonPath("$._links.last").exists())
           .andExpect(jsonPath("$.page").exists());
    }

    @Test
    public void getSupplier() throws Exception{
        mvc.perform(get(ApiUrls.ROOT_URL_SUPPLIERS + "/1"))
           .andExpect(status().isOk())
           .andExpect(content().contentType(contentType))
           .andExpect(jsonPath("$.name", is("Test Supplier 1")))
           .andExpect(jsonPath("$._links.self").exists());

        mvc.perform(get(ApiUrls.ROOT_URL_SUPPLIERS + "/10"))
           .andExpect(status().isNotFound());
    }

    @Test
    public void createAndDeleteSupplier() throws Exception{
        Supplier supplier = new Supplier("Test Supplier 4","Test Contact Person 4","LOCAL");
        MvcResult mvcResult = mvc.perform(post(ApiUrls.ROOT_URL_SUPPLIERS)
                                                  .content(supplier.toJsonString())
                                                  .contentType(MediaType.APPLICATION_JSON_UTF8))
                                 .andExpect(status().isCreated())
                                 .andReturn();
        String locationUri = mvcResult.getResponse().getHeader("Location");
        assertTrue(locationUri.contains(ApiUrls.ROOT_URL_SUPPLIERS));

        int idx = locationUri.lastIndexOf('/');
        String id = locationUri.substring(idx+1);

        mvc.perform(get(ApiUrls.ROOT_URL_SUPPLIERS + "/{id}",id))
           .andExpect(status().isOk())
           .andExpect(content().contentType(contentType))
           .andExpect(jsonPath("$.name", is("Test Supplier 4")))
           .andExpect(jsonPath("$.contactPerson", is("Test Contact Person 4")))
           .andExpect(jsonPath("$.supplierType", is("LOCAL")));

        mvc.perform(delete(ApiUrls.ROOT_URL_SUPPLIERS + "/{id}", id))
           .andExpect(status().isNoContent());

        mvc.perform(get(ApiUrls.ROOT_URL_SUPPLIERS + "/{id}", id))
           .andExpect(status().isNotFound());
    }

    @Test
    public void createSupplierBadRequest() throws Exception{
        Supplier supplier = new Supplier();
        mvc.perform(post(ApiUrls.ROOT_URL_SUPPLIERS)
                            .content(supplier.toJsonString())
                            .contentType(MediaType.APPLICATION_JSON_UTF8))
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath("$", hasSize(1)));

        //Test each fields one by one
        supplier = new Supplier("","Test Contact Person","LOCAL");
        mvc.perform(post(ApiUrls.ROOT_URL_SUPPLIERS)
                            .content(supplier.toJsonString())
                            .contentType(MediaType.APPLICATION_JSON_UTF8))
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath("$", hasSize(1)))
           .andExpect(jsonPath("$[0].field", is("name")))
           .andExpect(jsonPath("$[0].message", containsString("empty")));

//        supplier = new Supplier("Test Supplier","","LOCAL");
//        mvc.perform(post(ApiUrls.ROOT_URL_SUPPLIERS)
//                            .content(supplier.toJsonString())
//                            .contentType(MediaType.APPLICATION_JSON_UTF8))
//           .andExpect(status().isBadRequest())
//           .andExpect(jsonPath("$", hasSize(1)))
//           .andExpect(jsonPath("$[0].field", is("contactPerson")))
//           .andExpect(jsonPath("$[0].message", containsString("Contact Person cannot be empty")));
//
//        supplier = new Supplier("Test Supplier","Test Contact Person","");
//        mvc.perform(post(ApiUrls.ROOT_URL_SUPPLIERS)
//                            .content(supplier.toJsonString())
//                            .contentType(MediaType.APPLICATION_JSON_UTF8))
//           .andExpect(status().isBadRequest())
//           .andExpect(jsonPath("$", hasSize(1)))
//           .andExpect(jsonPath("$[0].field", is("supplierType")))
//           .andExpect(jsonPath("$[0].message", containsString("[NON_LOCAL,LOCAL]")));

    }

}

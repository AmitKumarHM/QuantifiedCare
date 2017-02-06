package com.kiwi.spring.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.jayway.jsonpath.JsonPath;
import com.qc.spring.configs.Configuration;
import com.qc.spring.controllers.UserController;
import com.qc.spring.utils.Encrypter;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:root-context.xml","classpath:/spring/spring-database.xml"})
@WebAppConfiguration
public class UserTestController {

	@Autowired
	Configuration env;

	@Autowired
    UserController userController;
	
	@Autowired
	SessionFactory sessionFactory;
	
    private MockMvc mockMvc;
    
    @Before
    public void setup() {
 
        // Process mock annotations
        MockitoAnnotations.initMocks(this);
 
        // Setup Spring test in standalone mode
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }
    
    @Test
    public void testLogin() throws Exception {

    	String key = env.get("AES_KEY");
    	String json = "{ \"email\": \"aa@kiwitech.com\",\"password\": \"kiwitech\"}";

    	String data = Encrypter.encrypt(json, key);
    	System.out.println("--------------------working....");

    	 MvcResult mvcResult = mockMvc.perform(post("/users/login").param("EncryptedValue", data))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    	 String response = mvcResult.getResponse().getContentAsString();
    	 
    	 assertNotNull(response);
    	 String result = Encrypter.decrypt(response, key);
    	 System.out.println(result);
    	 
    	 assertEquals(JsonPath.read(result, "$.result"), "success");
    }
    
    @Test
    public void testLogout() throws Exception {

    	String key = env.get("AES_KEY");
    	String json = "{ \"access_token\": \"32a1sd31asd321asd32asd32\"}";

    	String data = Encrypter.encrypt(json, key);
    	System.out.println("--------------------working....");

    	 MvcResult mvcResult = mockMvc.perform(post("/users/logout").param("EncryptedValue", data))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    	 String response = mvcResult.getResponse().getContentAsString();
    	 
    	 assertNotNull(response);
    	 String result = Encrypter.decrypt(response, key);
    	 System.out.println(result);
    	 
    	 assertEquals(JsonPath.read(result, "$.result"), "success");
    }
    
    @Test
    public void testChangePassword() throws Exception {

    	String key = env.get("AES_KEY");
    	String json = "{\"access_token\":\"907373940350511415875796\",\"user_id\":\"f61f1773a309e7a720a1f93387942ad0\", \"password\": \"akash\"}";

    	String data = Encrypter.encrypt(json, key);
    	System.out.println("--------------------working....");

    	 MvcResult mvcResult = mockMvc.perform(post("/users/change_pwd").param("EncryptedValue", data))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    	 String response = mvcResult.getResponse().getContentAsString();
    	 
    	 assertNotNull(response);
    	 String result = Encrypter.decrypt(response, key);
    	 System.out.println(result);
    	
    	 assertEquals(JsonPath.read(result, "$.result"), "success");
    }
    
    @Test
    public void testResetForgotPassword() throws Exception {

    	String key = env.get("AES_KEY");
    	String json = "{\"username\":\"ajay.gupta@kiwitech.com\"}";

    	String data = Encrypter.encrypt(json, key);
    	System.out.println("--------------------working....");

    	 MvcResult mvcResult = mockMvc.perform(post("/users/forgot_pwd").param("EncryptedValue", data))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    	 String response = mvcResult.getResponse().getContentAsString();
    	 
    	 assertNotNull(response);
    	 String result = Encrypter.decrypt(response, key);
    	 System.out.println(result);
    	
    	 assertEquals(JsonPath.read(result, "$.result"), "success");
    }
}

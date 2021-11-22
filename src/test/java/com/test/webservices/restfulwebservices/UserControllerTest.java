package com.test.webservices.restfulwebservices;

import com.test.webservices.restfulwebservices.webapp.controller.UserResource;
import com.test.webservices.restfulwebservices.webapp.dto.User;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = UserResource.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserResource userResource;

    User mockUser = new User(4, "David", "Walker", "david7@gmail.com");
    String exampleUserJson = "[{\n" +
            "        \"id\": 4,\n" +
            "        \"name\": \"David\",\n" +
            "        \"surname\": \"Walker\",\n" +
            "        \"email\": \"david7@gmail.com\",\n" +
            "        \"links\": []\n" +
            "    } \n" +
            "]";


    @Test
    @Tag("UnitTest")
    public void retrieveDetailsForUser() throws Exception {
        Mockito.when(userResource.retrieveSpecificUserById(Mockito.anyInt()))
                .thenReturn(mockUser);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/4")
                .accept(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        System.out.println(result.getResponse());
        String expected = "{\"id\":4,\"name\":\"David\",\"surname\":\"Walker\",\"email\":\"david7@gmail.com\"}";

        JSONAssert.assertEquals(expected, result.getResponse()
                .getContentAsString(), true);
    }

    @Test
    @Tag("LongUnitTest")
    public void checkUserDetails() throws Exception {
        Mockito.when(userResource.retrieveSpecificUserById(Mockito.anyInt()))
                .thenReturn(mockUser);

        mockMvc.perform(get("/users/4")).andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$.id").value("4"))
                .andExpect(jsonPath("$.name").value("David"))
                .andExpect(jsonPath("$.surname").value("XXXWalker"));

    }
    @Test
    @Tag("LongUnitTest")
    public void checkUserEmail() throws Exception {
        Mockito.when(userResource.retrieveSpecificUserById(Mockito.anyInt()))
                .thenReturn(mockUser);

        mockMvc.perform(get("/users/4")).andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$.email").value("david7@gmail.com"));
    }
}

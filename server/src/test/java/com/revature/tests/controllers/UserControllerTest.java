package com.revature.tests.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.mand.project2.controllers.UserController;
import com.revature.mand.project2.entities.Patron;
import com.revature.mand.project2.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(locations = {"classpath:test-application-context.xml"})
@WebAppConfiguration
public class UserControllerTest {

    //Mocker object
    private MockMvc mock;
    //Mock Services
    private UserService userMock;

    ObjectMapper om;
    Patron mockPatron;

    //Setup methods
    @Before
    public void setup() {
        userMock = mock(UserService.class);

        mock = MockMvcBuilders.standaloneSetup(new UserController(userMock)).build();
        om = new ObjectMapper();
        mockPatron = new Patron();

        mockPatron.setFirstName("Testy");
        mockPatron.setLastName("Testerson");
        mockPatron.setEmail("test@testerson.com");
        mockPatron.setUsername("iAmError");
    }

    //Test Methods

    //getUserByUserName -- Test
    @Test
    public void getUserByUserName_shouldReturnOk() throws Exception {
        //Convert POJO to JSON
        String testJSON = om.writeValueAsString(mockPatron);
        when(userMock.getUserByUserName(mockPatron.getUsername()))
                .thenReturn(testJSON);
        mock.perform(get("/get-user")
                .param("username", mockPatron.getUsername()))
                .andExpect(status().isOk());
    }
    @Test
    public void getUserByUserName_shouldReturnUNAUTHORIZED() throws Exception {
        when(userMock.getUserByUserName(mockPatron.getUsername()))
                .thenReturn("");
        mock.perform(get("/get-user")
                .param("username", mockPatron.getUsername()))
                .andExpect(status().isUnauthorized());
    }
}
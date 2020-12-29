package com.revature.tests.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.mand.project2.controllers.SignupController;
import com.revature.mand.project2.entities.Patron;
import com.revature.mand.project2.services.SignupService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(locations = {"classpath:test-application-context.xml"})
@WebAppConfiguration
public class SignupControllerTest {

    //Mocker object
    private MockMvc mock;
    //Mock Services
    private SignupService signupMock;

    ObjectMapper om;
    Patron mockPatron;

    //Setup methods
    @Before
    public void setup() {
        signupMock = mock(SignupService.class);

        mock = MockMvcBuilders.standaloneSetup(new SignupController(signupMock)).build();
        om = new ObjectMapper();
        mockPatron = new Patron();

        mockPatron.setFirstName("Testy");
        mockPatron.setLastName("Testerson");
        mockPatron.setEmail("test@testerson.com");
        mockPatron.setUsername("iAmError");
    }

    //Test Methods

    //registerUser -- Tests
    @Test
    public void registerUser_shouldReturnOk() throws Exception {
        //Convert POJO to JSON
        String testJSON = om.writeValueAsString(mockPatron);

        when(signupMock.registerUser(testJSON))
                .thenReturn(true);
        mock.perform(post("/signup")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(testJSON))
                .andExpect(status().isOk());
    }
    @Test
    public void registerUser_shouldReturnUNAUTHORIZED() throws Exception {
        //Convert POJO to JSON
        String testJSON = om.writeValueAsString(mockPatron);

        when(signupMock.registerUser(testJSON))
                .thenReturn(false);
        mock.perform(post("/signup")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(testJSON))
                .andExpect(status().isUnauthorized());
    }
}
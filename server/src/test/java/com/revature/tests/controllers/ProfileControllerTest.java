package com.revature.tests.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.mand.project2.controllers.ProfileController;
import com.revature.mand.project2.entities.Patron;
import com.revature.mand.project2.services.EditProfileService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(locations = {"classpath:test-application-context.xml"})
@WebAppConfiguration
public class ProfileControllerTest {

    //Mocker object
    private MockMvc mock;
    //Mock Services
    private EditProfileService editProfileMock;

    ObjectMapper om;
    Patron mockPatron;

    //Setup methods
    @Before
    public void setup() {
        editProfileMock = mock(EditProfileService.class);

        mock = MockMvcBuilders.standaloneSetup(new ProfileController(editProfileMock)).build();
        om = new ObjectMapper();
        mockPatron = new Patron();

        mockPatron.setFirstName("Testy");
        mockPatron.setLastName("Testerson");
        mockPatron.setEmail("test@testerson.com");
        mockPatron.setUsername("iAmError");
    }

    //Test methods

    //editProfile -- Tests
    @Test
    public void editProfile_shouldReturnOK() throws Exception {
        //Convert POJO to JSON
        String testJSON = om.writeValueAsString(mockPatron);

        when(editProfileMock.editProfile(testJSON, 20))
                .thenReturn(true);
        mock.perform(put("/update-profile?userId=20")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(testJSON))
                .andExpect(status().isOk());
    }
    @Test
    public void editProfile_shouldReturnINTERNAL_SERVER_ERROR() throws Exception {
        //Convert POJO to JSON
        String testJSON = om.writeValueAsString(mockPatron);

        when(editProfileMock.editProfile(testJSON, 20))
                .thenReturn(false);
        mock.perform(put("/update-profile?userId=20")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(testJSON))
                .andExpect(status().isInternalServerError());
    }
}
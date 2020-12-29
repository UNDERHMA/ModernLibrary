package com.revature.tests.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.mand.project2.controllers.ReservedBookController;
import com.revature.mand.project2.entities.Book;
import com.revature.mand.project2.services.*;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(locations = {"classpath:test-application-context.xml"})
@WebAppConfiguration
public class ReservedBookControllerTest {

    //Mocker object
    private MockMvc mock;
    //Mock Services
    private AddReservedBookService addReservedBookMock;
    private RetrieveReservedBookService retrieveReservedBookMock;
    private RetrieveReturnedBookService retrieveReturnedBookMock;
    private ReturnReservedBookService returnReservedBookMock;

    ObjectMapper om;
    Book mockBook;

    //Setup methods
    @Before
    public void setup() {
        addReservedBookMock = mock(AddReservedBookService.class);
        retrieveReservedBookMock = mock(RetrieveReservedBookService.class);
        retrieveReturnedBookMock = mock(RetrieveReturnedBookService.class);
        returnReservedBookMock = mock(ReturnReservedBookService.class);

        mock = MockMvcBuilders.standaloneSetup(new ReservedBookController(addReservedBookMock, retrieveReservedBookMock, retrieveReturnedBookMock, returnReservedBookMock)).build();
        om = new ObjectMapper();
        mockBook = new Book();

        //Create Mock Book Object
        mockBook.setIsbn("9781681495057");
        mockBook.setTitle("Ignatius Bible, 2nd Edition");
        mockBook.setAuthor("Unknown");
        mockBook.setImage("http://books.google.com/books/content?id=mU-JCwAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api");
        mockBook.setDescription("The Bible");
    }

    //Testing methods

    //addCartBook -- Tests
    @Test
    public void addReservedBooks_shouldReturnOK() throws Exception {
        //Convert POJO to JSON
        String testJSON = om.writeValueAsString(mockBook);

        //Run test
        when(addReservedBookMock.addReservedBooks(testJSON, 20))
                .thenReturn("Your books have been reserved");
        mock.perform(post("/reserved/add-all-reserved?userId=20")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(testJSON))
                .andExpect(status().isOk());
    }
    @Test
    public void addReservedBooks_shouldReturnINTERNAL_SERVER_ERROR() throws Exception {
        //Convert POJO to JSON
        String testJSON = om.writeValueAsString(mockBook);

        //Run test
        when(addReservedBookMock.addReservedBooks(testJSON, 20))
                .thenReturn("An error occurred processing your request");
        mock.perform(post("/reserved/add-all-reserved?userId=20")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(testJSON))
                .andExpect(status().isInternalServerError());
    }
    //getCartBookById -- Tests
    @Test
    public void getReservedBookById_shouldReturnOK() throws Exception {
        //Convert POJO to JSON
        String testJSON = om.writeValueAsString(mockBook);

        when(retrieveReservedBookMock.getReservedBooks(20))
                .thenReturn(testJSON);
        mock.perform(get("/reserved/get-reserved?userId=20"))
                .andExpect(status().isOk());
    }
    @Test
    public void getReservedBookById_shouldReturnINTERNAL_SERVER_ERROR() throws Exception {
        when(retrieveReservedBookMock.getReservedBooks(20))
                .thenReturn("");
        mock.perform(get("/reserved/get-reserved?userId=20"))
                .andExpect(status().isInternalServerError());
    }
    //getReturnedBookById -- Tests
    @Test
    public void getReturnedBookById_shouldReturnOK() throws Exception {
        //Convert POJO to JSON
        String testJSON = om.writeValueAsString(mockBook);

        when(retrieveReturnedBookMock.getReturnedBooks(20))
                .thenReturn(testJSON);
        mock.perform(get("/reserved/get-returned?userId=20"))
                .andExpect(status().isOk());
    }
    @Test
    public void getReturnedBookById_shouldReturnINTERNAL_SERVER_ERROR() throws Exception {
        when(retrieveReturnedBookMock.getReturnedBooks(20))
                .thenReturn("");
        mock.perform(get("/reserved/get-returned?userId=20"))
                .andExpect(status().isInternalServerError());
    }

    //deleteCartBookByUserIdAndIsbn -- Tests
    @Test
    public void returnReservedBookByUserIdAndIsbn_shouldReturnOK() throws Exception {
        when(returnReservedBookMock.returnReservedBook(20, mockBook.getIsbn()))
                .thenReturn("Your book has been returned");
        mock.perform(delete("/reserved/delete-from-reserved?userId=20")
                .param("isbn", mockBook.getIsbn()))
                .andExpect(status().isOk());
    }
    @Test
    public void returnReservedBookByUserIdAndIsbn_shouldReturnINTERNAL_SERVER_ERROR() throws Exception {
        when(returnReservedBookMock.returnReservedBook(20, mockBook.getIsbn()))
                .thenReturn("Failed to return the book from the reservation.");
        mock.perform(delete("/reserved/delete-from-reserved?userId=20")
                .param("isbn", mockBook.getIsbn()))
                .andExpect(status().isInternalServerError());
    }
    //deleteCartBookByUserId -- Tests
    @Test
    public void returnReservedBookByUserId_shouldReturnOK() throws Exception {
        when(returnReservedBookMock.returnReservedBooks(20))
                .thenReturn("All books have been returned");
        mock.perform(delete("/reserved/delete-all-from-reserved?userId=20"))
                .andExpect(status().isOk());
    }
    @Test
    public void returnReservedBookByUserId_shouldReturnINTERNAL_SERVER_ERROR() throws Exception {
        when(returnReservedBookMock.returnReservedBooks(20))
                .thenReturn("Failed to return books from the reservation.");
        mock.perform(delete("/reserved/delete-all-from-reserved?userId=20"))
                .andExpect(status().isInternalServerError());
    }
}

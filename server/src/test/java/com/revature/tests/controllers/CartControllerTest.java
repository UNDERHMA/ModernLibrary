package com.revature.tests.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.mand.project2.controllers.CartController;
import com.revature.mand.project2.entities.Book;
import com.revature.mand.project2.services.AddBookToCartService;
import com.revature.mand.project2.services.DeleteCartBookService;
import com.revature.mand.project2.services.RetrieveCartBookService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(locations = {"classpath:test-application-context.xml"})
@WebAppConfiguration
public class CartControllerTest {

    //Mocker object
    private MockMvc mock;
    //Mock Services
    private AddBookToCartService addBookToCartMock;
    private RetrieveCartBookService retrieveCartBookMock;
    private DeleteCartBookService deleteCartBookMock;

    ObjectMapper om;
    Book mockBook;

    //Setup methods
    @Before
    public void setup() {
        addBookToCartMock = mock(AddBookToCartService.class);
        deleteCartBookMock = mock(DeleteCartBookService.class);
        retrieveCartBookMock = mock(RetrieveCartBookService.class);

        mock = MockMvcBuilders.standaloneSetup(new CartController(addBookToCartMock, retrieveCartBookMock, deleteCartBookMock)).build();
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
    public void addCartBook_shouldReturnOK() throws Exception {
        //Convert POJO to JSON
        String testJSON = om.writeValueAsString(mockBook);

        //Run test
        when(addBookToCartMock.addCartBook(testJSON, 20))
               .thenReturn("Successfully added book to cart");
        mock.perform(post("/cart/add-cart?userId=20")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(testJSON))
                .andExpect(status().isOk());
    }
    @Test
    public void addCartBook_shouldReturnINTERNAL_SERVER_ERROR() throws Exception {
        //Convert POJO to JSON
        String testJSON = om.writeValueAsString(mockBook);

        //Run test
        when(addBookToCartMock.addCartBook(testJSON, 20))
                .thenReturn("There was an error processing your request.");
        mock.perform(post("/cart/add-cart?userId=20")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(testJSON))
                .andExpect(status().isInternalServerError());
    }
    @Test
    public void addCartBook_shouldReturnUNAUTHORIZED() throws Exception {
        //Convert POJO to JSON
        String testJSON = om.writeValueAsString(mockBook);

        //Run test
        when(addBookToCartMock.addCartBook(testJSON, 20))
                .thenReturn("You are not eligible to reserve this book because you have reserved it in the past 90 days.");
        mock.perform(post("/cart/add-cart?userId=20")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(testJSON))
                .andExpect(status().isUnauthorized());
    }
    //getCartBookById -- Tests
    @Test
    public void getCartBookById_shouldReturnOK() throws Exception {
        //Convert POJO to JSON
        String testJSON = om.writeValueAsString(mockBook);

        when(retrieveCartBookMock.getCartBookById(20))
                .thenReturn(testJSON);
        mock.perform(get("/cart/get-cart?userId=20"))
                .andExpect(status().isOk());
    }
    @Test
    public void getCartBookById_shouldReturnINTERNAL_SERVER_ERROR() throws Exception {
        when(retrieveCartBookMock.getCartBookById(20))
                .thenReturn("");
        mock.perform(get("/cart/get-cart?userId=20"))
                .andExpect(status().isInternalServerError());
    }
    //deleteCartBookByUserIdAndIsbn -- Tests
    @Test
    public void deleteCartBookByUserIdAndIsbn_shouldReturnOK() throws Exception {
        when(deleteCartBookMock.deleteCartBook(20, mockBook.getIsbn()))
                .thenReturn("Book has been removed from cart.");
        mock.perform(delete("/cart/delete-from-cart?userId=20")
                .param("isbn", mockBook.getIsbn()))
                .andExpect(status().isOk());
    }
    @Test
    public void deleteCartBookByUserIdAndIsbn_shouldReturnINTERNAL_SERVER_ERROR() throws Exception {
        when(deleteCartBookMock.deleteCartBook(20, mockBook.getIsbn()))
                .thenReturn("Failed to retrieve books from the shopping cart.");
        mock.perform(delete("/cart/delete-from-cart?userId=20")
                .param("isbn", mockBook.getIsbn()))
                .andExpect(status().isInternalServerError());
    }
    //deleteCartBookByUserId -- Tests
    @Test
    public void deleteCartBookByUserId_shouldReturnOK() throws Exception {
        when(deleteCartBookMock.deleteCartBook(20))
                .thenReturn("All books have been removed from cart.");
        mock.perform(delete("/cart/delete-all-from-cart?userId=20"))
                .andExpect(status().isOk());
    }
    @Test
    public void deleteCartBookByUserId_shouldReturnINTERNAL_SERVER_ERROR() throws Exception {
        when(deleteCartBookMock.deleteCartBook(20))
                .thenReturn("Failed to delete the book from the shopping cart.");
        mock.perform(delete("/cart/delete-all-from-cart?userId=20"))
                .andExpect(status().isInternalServerError());
    }
}

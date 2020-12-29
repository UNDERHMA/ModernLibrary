package com.revature.tests.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.mand.project2.controllers.WishlistController;
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
public class WishlistControllerTest {

    //Mocker object
    private MockMvc mock;
    //Mock Services
    private AddWishlistBookService addWishlistBookMock;
    private RetrieveWishlistBookService retrieveWishlistBookMock;
    private DeleteWishlistBookService deleteWishlistBookMock;

    ObjectMapper om;
    Book mockBook;

    //Setup methods
    @Before
    public void setup() {
        addWishlistBookMock = mock(AddWishlistBookService.class);
        deleteWishlistBookMock = mock(DeleteWishlistBookService.class);
        retrieveWishlistBookMock = mock(RetrieveWishlistBookService.class);

        mock = MockMvcBuilders.standaloneSetup(new WishlistController(addWishlistBookMock, retrieveWishlistBookMock, deleteWishlistBookMock)).build();
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
    public void addWishlistBook_shouldReturnOK() throws Exception {
        //Convert POJO to JSON
        String testJSON = om.writeValueAsString(mockBook);

        //Run test
        when(addWishlistBookMock.addWishlistBook(testJSON, 20))
                .thenReturn("Successfully added to Wishlist");
        mock.perform(post("/wishlist/add-wish-list?userId=20")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(testJSON))
                .andExpect(status().isOk());
    }
    @Test
    public void addWishlistBook_shouldReturnINTERNAL_SERVER_ERROR() throws Exception {
        //Convert POJO to JSON
        String testJSON = om.writeValueAsString(mockBook);

        //Run test
        when(addWishlistBookMock.addWishlistBook(testJSON, 20))
                .thenReturn("There was an error processing your request.");
        mock.perform(post("/wishlist/add-wish-list?userId=20")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(testJSON))
                .andExpect(status().isInternalServerError());
    }
    @Test
    public void addWishlistBook_shouldReturnUNAUTHORIZED() throws Exception {
        //Convert POJO to JSON
        String testJSON = om.writeValueAsString(mockBook);

        //Run test
        when(addWishlistBookMock.addWishlistBook(testJSON, 20))
                .thenReturn("This book is already in your wishlist.");
        mock.perform(post("/wishlist/add-wish-list?userId=20")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(testJSON))
                .andExpect(status().isUnauthorized());
    }
    //getCartBookById -- Tests
    @Test
    public void getWishlistBookById_shouldReturnOK() throws Exception {
        //Convert POJO to JSON
        String testJSON = om.writeValueAsString(mockBook);

        when(retrieveWishlistBookMock.getWishlistBookById(20))
                .thenReturn(testJSON);
        mock.perform(get("/wishlist/get-wishlist?userId=20"))
                .andExpect(status().isOk());
    }
    @Test
    public void getWishlistBookById_shouldReturnINTERNAL_SERVER_ERROR() throws Exception {
        when(retrieveWishlistBookMock.getWishlistBookById(20))
                .thenReturn("");
        mock.perform(get("/wishlist/get-wishlist?userId=20"))
                .andExpect(status().isInternalServerError());
    }
    //deleteCartBookByUserIdAndIsbn -- Tests
    @Test
    public void deleteWishlistBookByUserIdAndIsbn_shouldReturnOK() throws Exception {
        when(deleteWishlistBookMock.deleteWishlistBook(20, mockBook.getIsbn()))
                .thenReturn("Successfully removed book from your wishlist.");
        mock.perform(delete("/wishlist/delete-from-wishlist?userId=20")
                .param("isbn", mockBook.getIsbn()))
                .andExpect(status().isOk());
    }
    @Test
    public void deleteWishlistBookByUserIdAndIsbn_shouldReturnINTERNAL_SERVER_ERROR() throws Exception {
        when(deleteWishlistBookMock.deleteWishlistBook(20, mockBook.getIsbn()))
                .thenReturn("An error occurred removing book from your wishlist.");
        mock.perform(delete("/wishlist/delete-from-wishlist?userId=20")
                .param("isbn", mockBook.getIsbn()))
                .andExpect(status().isInternalServerError());
    }
    //deleteCartBookByUserId -- Tests
    @Test
    public void deleteWishlistBookByUserId_shouldReturnOK() throws Exception {
        when(deleteWishlistBookMock.deleteWishlistBook(20))
                .thenReturn("Successfully removed all books from your wishlist.");
        mock.perform(delete("/wishlist/delete-all-from-wishlist?userId=20"))
                .andExpect(status().isOk());
    }
    @Test
    public void deleteWishlistBookByUserId_shouldReturnINTERNAL_SERVER_ERROR() throws Exception {
        when(deleteWishlistBookMock.deleteWishlistBook(20))
                .thenReturn("An error occurred removing all books from your wishlist.");
        mock.perform(delete("/wishlist/delete-all-from-wishlist?userId=20"))
                .andExpect(status().isInternalServerError());
    }
}

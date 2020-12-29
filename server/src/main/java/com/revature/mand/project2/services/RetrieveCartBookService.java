package com.revature.mand.project2.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.mand.project2.entities.CartBook;
import com.revature.mand.project2.repositories.CartBookRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <h3>RetrieveCartBookService</h3>
 * <p>Service layer class responsible for mapping database rows into JSON strings.</p>
 * @author Mason Underhill
 */
@Log4j2
@Service
public class RetrieveCartBookService {

    CartBookRepository cartBookRepository;

    @Autowired
    RetrieveCartBookService(CartBookRepository cartBookRepository) {
        this.cartBookRepository = cartBookRepository;
    }

    /**
     * <h4>getCartBookById</h4>
     * <p>Arranges all books from a cart belonging to a particular patron into a JSON string.</p>
     * @param userId The Patron id tied to the cart
     * @return The JSON string of books
     */
    @Transactional(readOnly = true)
    public String getCartBookById(int userId) {
        log.debug("Retrieving books in the shopping cart tied to the patron: " + userId);
        List<CartBook> cartBooks = cartBookRepository.getByUserId(userId);
        StringBuilder cartBooksString = new StringBuilder();
        cartBooksString.append("[");
        if(!cartBooks.isEmpty()) {
            log.info("Successfully retrieved books from the shopping cart. Converting into JSON...");
            for (int i = 0; i < cartBooks.size(); i++) {
                ObjectMapper om = new ObjectMapper();
                String bookString = "";
                try {
                    bookString = om.writeValueAsString(cartBooks.get(i));
                    cartBooksString.append(bookString);
                    if(i < cartBooks.size()-1) {
                        cartBooksString.append(",");
                    }
                } catch (JsonProcessingException e) {
                    log.error("Unable to convert shopping cart books into JSON!", e);
                }
            }
            cartBooksString.append("]");
            log.debug("JSON String result: " + cartBooksString);
            return cartBooksString.toString();
        }
        else {
            cartBooksString.append("]");
            log.info("No books found in the shopping cart! Returning blank JSON string.");
            return cartBooksString.toString();
        }
    }
}

package com.revature.mand.project2.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.mand.project2.entities.WishlistBook;
import com.revature.mand.project2.repositories.WishlistBookRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <h3>RetrieveWishlistBookService</h3>
 * <p>Service layer class responsible for mapping database rows into JSON strings.</p>
 * @author Mason Underhill
 */
@Log4j2
@Service
public class RetrieveWishlistBookService {

    WishlistBookRepository wishlistBookRepository;

    @Autowired
    RetrieveWishlistBookService(WishlistBookRepository wishlistBookRepository) {
        this.wishlistBookRepository = wishlistBookRepository;
    }

    /**
     * <h4>getWishlistBookById</h4>
     * <p>Arranges all books from a wishlist belonging to a particular patron into a JSON string.</p>
     *
     * @param userId The Patron id tied to the wishlist
     * @return The JSON string of books
     */
    @Transactional(readOnly = true)
    public String getWishlistBookById(int userId) {
        log.debug("Retrieving all books in the wishlist tied to the patron: " + userId);
        List<WishlistBook> wishlistBooks = wishlistBookRepository.getByUserId(userId);
        StringBuilder wishlistBooksString = new StringBuilder();
        wishlistBooksString.append("[");
        if(!wishlistBooks.isEmpty()) {
            log.info("Successfully retrieved books from the wishlist. Converting into JSON...");
            for (int i = 0; i < wishlistBooks.size(); i++) {
                ObjectMapper om = new ObjectMapper();
                String bookString = "";
                try {
                    bookString = om.writeValueAsString(wishlistBooks.get(i));
                    wishlistBooksString.append(bookString);
                    if(i < wishlistBooks.size()-1) {
                        wishlistBooksString.append(",");
                    }
                } catch (JsonProcessingException e) {
                    log.error("Unable to convert wishlist books into JSON!", e);
                }
            }
            wishlistBooksString.append("]");
            log.debug("JSON String result: " + wishlistBooksString);
            return wishlistBooksString.toString();
        }else {
            wishlistBooksString.append("]");
            log.info("No books found in the wishlist! Returning blank JSON string.");
            return wishlistBooksString.toString();
        }
    }
}

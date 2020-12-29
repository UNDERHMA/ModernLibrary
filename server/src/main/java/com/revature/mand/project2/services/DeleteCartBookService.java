package com.revature.mand.project2.services;

import com.revature.mand.project2.repositories.CartBookRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <h3>DeleteCartBookService</h3>
 * <p>Service layer class which can be called to process a book to be dropped to a patron's cart.</p>
 * @author Mason Underhill
 */
@Log4j2
@Service
public class DeleteCartBookService {

    CartBookRepository cartBookRepository;

    @Autowired
    DeleteCartBookService(CartBookRepository cartBookRepository) {
        this.cartBookRepository = cartBookRepository;
    }

    /**
     * <h4>deleteCartBook</h4>
     * <p>Drops a book from a cart belonging to a particular patron.</p>
     * @param userId The patron tied to the cart
     * @param isbn The book to be dropped from the cart
     * @return A boolean
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public String deleteCartBook(int userId, String isbn) {
        log.debug("Deleting the following book: " + isbn +", from the patron's (" + userId +") shopping cart!");
        if(cartBookRepository.deleteCartBook(userId, isbn)){
            return "Book has been removed from cart.";
        } else {
            return "An error occurred processing your request";
        }
    }

    /**
     * <h4>deleteCartBook</h4>
     * <p>Drops all books from a cart belonging to a particular patron.</p>
     * @param userId The patron tied to the cart
     * @return A boolean
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public String deleteCartBook(int userId) {
        log.debug("Deleting the all book from the patron's (" + userId +") shopping cart!");
        if(cartBookRepository.deleteCartBook(userId)){
            return "All books have been removed from cart.";
        } else {
            return "An error occurred processing your request";
        }
    }
}

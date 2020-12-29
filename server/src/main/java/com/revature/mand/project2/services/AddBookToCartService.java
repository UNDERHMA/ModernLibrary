package com.revature.mand.project2.services;

import com.revature.mand.project2.entities.Book;
import com.revature.mand.project2.entities.CartBook;
import com.revature.mand.project2.repositories.BookRepository;
import com.revature.mand.project2.repositories.CartBookRepository;
import com.revature.mand.project2.repositories.ReservedBookRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <h3>AddBookToCartService</h3>
 * <p>Service layer class which can be called to process a book to be added to a patron's cart.</p>
 * @author Mason Underhill
 */
@Log4j2
@Service
public class AddBookToCartService {

    BookRepository bookRepository;
    CartBookRepository cartBookRepository;
    ReservedBookRepository reservedBookRepository;
    InstantiateBookService instantiateBookService;

    @Autowired
    AddBookToCartService(CartBookRepository cartBookRepository,
                         InstantiateBookService instantiateBookService,
                         ReservedBookRepository reservedBookRepository,
                         BookRepository bookRepository) {
        this.cartBookRepository = cartBookRepository;
        this.instantiateBookService = instantiateBookService;
        this.reservedBookRepository = reservedBookRepository;
        this.bookRepository = bookRepository;
    }

    /**
     * <h4>addCartBook</h4>
     * <p>Creates a book object from a given JSON string and adds the book
     *    object to a cart belonging to a specified patron (userId).</p>
     * @param jsonString The JSON representation of the book being retrieved from the front-end
     * @param userId The patron tied to the cart
     * @return A boolean representing successful database persist.
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public String addCartBook(String jsonString, int userId) {
        log.debug("Initializing shopping cart Book from JSON...");
        Book book = instantiateBookService.instantiateBook(jsonString);
        if(book == null) {
            log.error("Failed to instantiate a book object from the given JSON data!");
            return "Invalid Book Input";
        }
        Book bookInDatabase = bookRepository.getByIsbn(book.getIsbn());
        if(bookInDatabase != null) {
            // user already has book in cart
            if(cartBookRepository.getCartBookByPatronIdAndIsbn(userId,book.getIsbn()) != null) {
                log.debug("Book is already in cart");
                return "Book is already in cart";
            }
            // check if two books have been checked out already (none available)
            if (bookInDatabase.getCopiesCheckedOut() == 2) {
                log.debug("Two copies of this book have already been checked out");
                return "Book is unavailable. All copies checked out.";
            }
            // logic to check if user checked out in last 90 or has one copy checked out
            boolean eligible = reservedBookRepository.eligibleToCheckout(userId, book.getIsbn());
            if (!eligible) {
                log.debug("AddBookToCartService - addCartBook - reserved in past 90 days");
                return "You are not eligible to reserve this book because you have" +
                        " reserved it in the past 90 days.";
            }
        }
        CartBook cartBook = new CartBook();
        cartBook.setPatronId(userId);
        if(bookInDatabase != null) cartBook.setBook(bookInDatabase);
        else cartBook.setBook(book);
        cartBook.setToCheckout(false);
        log.debug("Saving the following book to the cart table: " + cartBook.getBook().getTitle());
        if(cartBookRepository.save(cartBook)) {
            return "Successfully added book to cart";
        }else {
            return "There was an error processing your request.";
        }
    }
}

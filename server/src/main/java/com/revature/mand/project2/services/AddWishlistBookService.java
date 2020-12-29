package com.revature.mand.project2.services;

import com.revature.mand.project2.entities.Book;
import com.revature.mand.project2.entities.WishlistBook;
import com.revature.mand.project2.repositories.BookRepository;
import com.revature.mand.project2.repositories.WishlistBookRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <h3>AddWishlistBookService</h3>
 * <p>Service layer class which can be called to process a book to be added to a patron's wishlist.</p>
 * @author Mason Underhill
 */
@Log4j2
@Service
public class AddWishlistBookService {

    WishlistBookRepository wishlistBookRepository;
    InstantiateBookService instantiateBookService;
    BookRepository bookRepository;

    @Autowired
    AddWishlistBookService(WishlistBookRepository wishlistBookRepository,
                           InstantiateBookService instantiateBookService,
                           BookRepository bookRepository) {
        this.wishlistBookRepository = wishlistBookRepository;
        this.instantiateBookService = instantiateBookService;
        this.bookRepository = bookRepository;
    }

    /**
     * <h4>addWishlsitBook</h4>
     * <p>Creates a book object from a given JSON string and adds the book
     *    object to a wishlist belonging to a specified patron (userId).</p>
     * @param jsonString The JSON representation of the book being retrieved from the front-end
     * @param userId The patron tied to the wishlist
     * @return A boolean representing successful database persist.
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public String addWishlistBook(String jsonString, int userId) {
        log.debug("Initializing wishlist Book object from JSON...");
        Book book = instantiateBookService.instantiateBook(jsonString);
        if(book == null) {
            log.error("Failed to interpret JSON string into a Book object!");
            return "There was an error processing your request.";
        }
        if(wishlistBookRepository.getWishlistBookByPatronIdAndIsbn(userId,book.getIsbn()) != null) {
            log.debug("Book is already in cart");
            return "Book is already in wishlist";
        }
        Book bookInDatabase = bookRepository.getByIsbn(book.getIsbn());
        WishlistBook wishlistBook = new WishlistBook();
        wishlistBook.setPatronId(userId);
        if(bookInDatabase != null) wishlistBook.setBook(bookInDatabase);
        else wishlistBook.setBook(book);
        log.debug("Persisted the following book to the wishlist: " + wishlistBook.getBook().getTitle());
        boolean added = wishlistBookRepository.save(wishlistBook);
        if(added){
            return "Successfully added to Wishlist";
        }else {
            return "There was an error processing your request.";
        }
    }
}

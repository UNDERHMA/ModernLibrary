package com.revature.mand.project2.services;

import com.revature.mand.project2.entities.Book;
import com.revature.mand.project2.entities.ReservedBook;
import com.revature.mand.project2.repositories.ReservedBookRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <h3>AddReservedBookService</h3>
 * <p>Service layer class which can be called to process a book to be added to a patron's reservation.</p>
 * @author Mason Underhill
 */
@Log4j2
@Service
public class AddReservedBookService {

    ReservedBookRepository reservedBookRepository;
    InstantiateBookService instantiateBookService;

    @Autowired
    AddReservedBookService(ReservedBookRepository reservedBookRepository,
                           InstantiateBookService instantiateBookService) {
        this.reservedBookRepository = reservedBookRepository;
        this.instantiateBookService = instantiateBookService;
    }

    /**
     * <h4>addReservedBooks</h4>
     * <p>Creates a book object from a given JSON string and adds the book
     *    object to a reservation belonging to a specified patron (userId).</p>
     * @param jsonString The JSON representation of the book being retrieved from the front-end
     * @param userId The patron tied to the reservation
     * @return A boolean representing successful database persist.
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public String addReservedBooks(String jsonString, int userId) {
        log.debug("Initializing reservation Book array from JSON...");
        Book[] books = instantiateBookService.instantiateBookArray(jsonString);
        if(books == null) {
            log.error("Failed to interpret JSON string into a Book array!");
            return "An error occurred processing your request";
        }
        boolean result = true;
        for(Book b : books) {
            // increment number of books checked out, and set ReservedBook attributes
            b.setCopiesCheckedOut(b.getCopiesCheckedOut()+1);
            ReservedBook reservedBook = new ReservedBook();
            reservedBook.setPatronId(userId);
            reservedBook.setBook(b);
            reservedBook.setLate(false);
            boolean success = reservedBookRepository.save(reservedBook);
            if (success == false) {
                log.error("Failed to persist book in the database!");
                return "An error occurred processing your request";
            }
            log.info("Successfully persisted the following book: " + b.getTitle());
        }
        return "Your books have been reserved";
    }
}

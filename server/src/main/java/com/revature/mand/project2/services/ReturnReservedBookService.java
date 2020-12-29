package com.revature.mand.project2.services;

import com.revature.mand.project2.entities.ReservedBook;
import com.revature.mand.project2.repositories.ReservedBookRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <h3>DeleteReservedBookService</h3>
 * <p>Service layer class which can be called to process a book to be dropped to a patron's reservation.</p>
 * @author Mason Underhill
 */
@Log4j2
@Service
public class ReturnReservedBookService {

    ReservedBookRepository reservedBookRepository;

    @Autowired
    ReturnReservedBookService(ReservedBookRepository reservedBookRepository) {
        this.reservedBookRepository = reservedBookRepository;
    }

    /**
     * <h4>returnReservedBook</h4>
     * <p>Returns a book from a reservation belonging to a particular patron.</p>
     * @param userId The patron tied to the reservation
     * @param isbn The book to be dropped from the reservation
     * @return A boolean
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public String returnReservedBook(int userId, String isbn) {
        log.debug("Returning reserved book with the ISBN: " + isbn);
        ReservedBook reservedBook = reservedBookRepository.returnReservedBook(userId,isbn);
        if(reservedBook == null) {
            log.error("Database error, a Reserved Book that should exist cannot be found");
            return "An error occurred processing your request";
        }
        reservedBook.setReturnDate(new java.sql.Date(new java.util.Date().getTime()));
        if(reservedBook.getDueDate().compareTo(reservedBook.getReturnDate()) < 0) {
            reservedBook.setLate(true);
        }
        int copiesCheckoutOut = reservedBook.getBook().getCopiesCheckedOut();
        copiesCheckoutOut--;
        reservedBook.getBook().setCopiesCheckedOut(copiesCheckoutOut);
        return "Your book has been returned";
    }

    /**
     * <h4>deleteReservedBook</h4>
     * <p>Returns all books from a reservation belonging to a particular patron.</p>
     * @param userId The patron tied to the reservation
     * @return A boolean
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public String returnReservedBooks(int userId) {
        log.debug("Returning all reserved book borrowed by the following user: " +userId);
        List<ReservedBook> reservedBooks = reservedBookRepository.returnReservedBooks(userId);
        if(reservedBooks == null) {
            log.error("Database error, Reserved Books that should exist cannot be found");
            return "An error occurred processing your request";
        }
        for(ReservedBook reservedBook : reservedBooks) {
            reservedBook.setReturnDate(new java.sql.Date(new java.util.Date().getTime()));
            if (reservedBook.getDueDate().compareTo(reservedBook.getReturnDate()) < 0) {
                reservedBook.setLate(true);
            }
            int copiesCheckoutOut = reservedBook.getBook().getCopiesCheckedOut();
            copiesCheckoutOut--;
            reservedBook.getBook().setCopiesCheckedOut(copiesCheckoutOut);
            reservedBookRepository.save(reservedBook);
        }
        return "All books have been returned";
    }
}

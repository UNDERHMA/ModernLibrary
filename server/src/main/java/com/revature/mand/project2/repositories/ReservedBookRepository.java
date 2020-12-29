package com.revature.mand.project2.repositories;

import com.revature.mand.project2.entities.ReservedBook;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 *<h3>ReservedBookRepository</h3>
 * <p>Repository layer class defining several methods for executing
 *    CRUD functions on the repository table.</p>
 *
 * @author Mason Underhill
 */
@Repository
@Log4j2
public class ReservedBookRepository {

    SessionFactory sessionFactory;

    @Autowired
    ReservedBookRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * <h4>save</h4>
     * <p>Saves book to the reservation table.</p>
     * @param reservedBook The book to be persisted
     * @return A boolean representing whether the book was successfully persisted.
     */
    public boolean save(ReservedBook reservedBook) {
        log.debug("Saving book: " + reservedBook.getBook().getTitle() + ", to database...");
        Session session = sessionFactory.getCurrentSession();
        session.save(reservedBook);
        return true;
    }

    /**
     * <h4>getReservedBooks</h4>
     * <p>Retrieves all reserved books belonging to a particular patron (userId).</p>
     * @param UserId The patron tied to the wishlist
     * @return A list of all retrieved books.
     */
    public boolean eligibleToCheckout(int UserId, String isbn) {
        log.debug("Checking whether book can be added to cart - Patron ID: " + UserId);
        Session session = sessionFactory.getCurrentSession();
        Query q = session.getNamedNativeQuery("isPatronEligible");
        q.setParameter("patronid",UserId);
        q.setParameter("isbn",isbn);
        List<ReservedBook> reservedBooks = q.getResultList();
        if(reservedBooks.isEmpty()) return true;
        else return false;
    }

    /**
     * <h4>getReservedBooks</h4>
     * <p>Retrieves all reserved books belonging to a particular patron (userId).</p>
     * @param UserId The patron tied to the wishlist
     * @return A list of all retrieved books.
     */
    public List<ReservedBook> getReservedBooks(int UserId) {
        log.debug("Retrieving all books in the reservation associated with Patron ID: " + UserId);
        Session session = sessionFactory.getCurrentSession();
        Query q = session.getNamedQuery("getReservedBookByPatronId");
        q.setParameter("patronid", UserId);
        List<ReservedBook> reservedBooks = q.getResultList();
        if (reservedBooks.isEmpty()) {
            log.warn("Failed to retrieve any books.");
            return new ArrayList<ReservedBook>();
        } else {
            log.info("Successfully retrieved books from the patron's reservation.");
            return reservedBooks;
        }
    }
    /**
     * <h4>getReturnedBooks</h4>
     * <p>Retrieves all returned books belonging to a particular patron (userId).</p>
     * @param userId The patron tied to the wishlist
     * @return A list of all retrieved books.
     */
    public List<ReservedBook> getReturnedBooks(int userId) {
        log.debug("Retrieving all returned books associated with Patron ID: " + userId);
        Session session = sessionFactory.getCurrentSession();
        Query q = session.getNamedQuery("getReturnedBookByPatronId");
        q.setParameter("patronid",userId);
        List<ReservedBook> reservedBooks = q.getResultList();
        if(reservedBooks.isEmpty()) {
            log.warn("Failed to retrieve any returned books.");            
            return new ArrayList<ReservedBook>();
        }
        else {
            log.info("Successfully retrieved all returned books from the patron's reservation.");
            return reservedBooks;
        }
    }

    /**
     * <h4>deleteReservedBook</h4>
     * <p>Deletes a book from a reservation belonging to a particular patron (userId).</p>
     * @param userId The patron tied to the reservation
     * @param isbn The book to be deleted from the table
     * @return A boolean value based on whether the table was successfully affected or not
     */
    public ReservedBook returnReservedBook(int userId, String isbn) {
        log.debug("Returning book with ISBN: " + isbn + ", from the reservation associated with Patron ID: " + userId);
        Session session = sessionFactory.getCurrentSession();
        Query q = session.getNamedQuery("getReservedBookByIsbnAndPatronId");
        q.setParameter("patronid",userId);
        q.setParameter("isbn",isbn);
        ReservedBook reservedBook = (ReservedBook) q.getSingleResult();
        if(reservedBook == null) return null; else return reservedBook;
    }

    /**
     * <h4>deleteReservedBook</h4>
     * <p>Delete all books from a reservation belonging to a particular patron (userId).</p>
     * @param userId The patron tied to the reservation
     * @return A boolean value based on whether the table was successfully affected or not
     */
    public List<ReservedBook> returnReservedBooks(int userId) {
        log.debug("Returning all books from the reservation associated with Patron ID: " + userId);
        Session session = sessionFactory.getCurrentSession();
        Query q = session.getNamedQuery("getReservedBookByPatronId");
        q.setParameter("patronid",userId);
        List<ReservedBook> reservedBooks = q.getResultList();
        if(reservedBooks == null) return null; else return reservedBooks;
    }

}

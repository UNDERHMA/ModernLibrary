package com.revature.mand.project2.repositories;

import com.revature.mand.project2.entities.CartBook;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 *<h3>CartBookRepository</h3>
 * <p>Repository layer class defining several methods for executing
 *    CRUD functions on the cart table.</p>
 *
 * @author Mason Underhill
 */
@Repository
@Log4j2
public class CartBookRepository {

    SessionFactory sessionFactory;

    @Autowired
    CartBookRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * <h4>save</h4>
     * <p>Saves book to the cart table.</p>
     * @param cartBook The book to be persisted to the table
     * @return true
     */
    public boolean save(CartBook cartBook) {
        log.debug("Saving book: " + cartBook.getBook().getTitle() + ", to database...");
        Session session = sessionFactory.getCurrentSession();
        session.merge(cartBook);
        return true;
    }

    /**
     * <h4>getCartBookByPatronIdAndIsbn</h4>
     * <p>Retrieves all books from a cart belonging to a particular patron (userId).</p>
     * @param userId The patron tied to the cart
     * @param isbn The patron tied to the cart
     * @return A CartBook Object.
     */
    public CartBook getCartBookByPatronIdAndIsbn(int userId, String isbn) {
        log.debug("Retrieving all books with isbn "+isbn+" associated with Patron ID: " + userId);
        Session session = sessionFactory.getCurrentSession();
        Query q = session.getNamedQuery("getCartBookByPatronIdAndIsbn");
        q.setParameter("patronid",userId);
        q.setParameter("isbn",isbn);
        List<CartBook> cartBookList = q.getResultList();
        if(cartBookList.isEmpty()) {
            log.warn("No book found!");
            return null;
        }
        else {
            log.info("Book successfully retrieved!");
            return cartBookList.get(0);
        }
    }

    /**
     * <h4>getByUserId</h4>
     * <p>Retrieves all books from a cart belonging to a particular patron (userId).</p>
     * @param UserId The patron tied to the cart
     * @return A list of all retrieved books.
     */
    public List<CartBook> getByUserId(int UserId) {
        log.debug("Retrieving all books in the cart associated with Patron ID: " + UserId);
        Session session = sessionFactory.getCurrentSession();
        Query q = session.getNamedQuery("getCartBookByPatronId");
        q.setParameter("patronid",UserId);
        List<CartBook> cartBooks = q.getResultList();
        if(cartBooks.isEmpty()) {
            log.warn("No books found!");
            return new ArrayList<CartBook>();
        }
        else {
            log.info("Books successfully retrieved!");
            return cartBooks;
        }
    }

    /**
     * <h4>deleteCartBook</h4>
     * <p>Deletes a book from a cart belonging to a particular patron (userId).</p>
     * @param userId The patron tied to the cart
     * @param isbn The book to be deleted from the table
     * @return A boolean value based on whether the table was successfully affected or not
     */
    public boolean deleteCartBook(int userId, String isbn) {
        log.debug("Deleting book with ISBN: " + isbn + ", from the cart associated with Patron ID: " + userId);
        Session session = sessionFactory.getCurrentSession();
        Query q = session.getNamedNativeQuery("deleteCartBookByPatronIdAndIsbn");
        q.setParameter("patronid",userId);
        q.setParameter("isbn",isbn);
        int rowsAffected = q.executeUpdate();
        if(rowsAffected > 0) {
            log.info("Successfully removed the book!");
            return true;
        } else {
            log.warn("Failed to remove the book!");
            return false;
        }
    }

    /**
     * <h4>deleteCartBook</h4>
     * <p>Delete all books from a cart belonging to a particular patron (userId).</p>
     * @param userId The patron tied to the cart
     * @return A boolean value based on whether the table was successfully affected or not
     */
    public boolean deleteCartBook(int userId) {
        log.debug("Deleting all books from the cart associated with Patron ID: " + userId);
        Session session = sessionFactory.getCurrentSession();
        Query q = session.getNamedNativeQuery("deleteCartBookByPatronId");
        q.setParameter("patronid",userId);
        int rowsAffected = q.executeUpdate();
        if(rowsAffected > 0) {
            log.info("Successfully removed books from the cart!");
            return true;
        } else {
            log.warn("Failed to remove books!");
            return false;
        }
    }
}

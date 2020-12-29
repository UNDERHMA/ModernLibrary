package com.revature.mand.project2.repositories;

import com.revature.mand.project2.entities.WishlistBook;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 *<h3>WishlistBookRepository</h3>
 * <p>Repository layer class defining several methods for executing
 *    CRUD functions on the wishlist table.</p>
 *
 * @author Mason Underhill
 */
@Repository
@Log4j2
public class WishlistBookRepository {

    SessionFactory sessionFactory;

    @Autowired
    WishlistBookRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * <h4>save</h4>
     * <p>Saves book to the wishlist table.</p>
     * @param wishlistBook The book to be persisted to the table.
     * @return A boolean representing whether the book is successfully persisted.
     */
    public boolean save(WishlistBook wishlistBook) {
        log.debug("Saving book: " + wishlistBook.getBook().getTitle() + ", to database...");
        Session session = sessionFactory.getCurrentSession();
        session.save(wishlistBook);
        return true;
    }

    /**
     * <h4>getWishlistBookByPatronIdAndIsbn</h4>
     * <p>Retrieves all books from a cart belonging to a particular patron (userId).</p>
     * @param userId The patron tied to the cart
     * @param isbn The patron tied to the cart
     * @return A WishlistBook Object.
     */
    public WishlistBook getWishlistBookByPatronIdAndIsbn(int userId, String isbn) {
        log.debug("Retrieving all books with isbn "+isbn+" associated with Patron ID: " + userId);
        Session session = sessionFactory.getCurrentSession();
        Query q = session.getNamedQuery("getWishlistBookByPatronIdAndIsbn");
        q.setParameter("patronid",userId);
        q.setParameter("isbn",isbn);
        List<WishlistBook> wishlistBookList = q.getResultList();
        if(wishlistBookList.isEmpty()) {
            log.warn("No book found!");
            return null;
        }
        else {
            log.info("Book successfully retrieved!");
            return wishlistBookList.get(0);
        }
    }

    /**
     * <h4>getByUserId</h4>
     * <p>Retrieves all books from a wishlist belonging to a particular patron (userId).</p>
     * @param userId The patron tied to the wishlist
     * @return A list of all retrieved books.
     */
    public List<WishlistBook> getByUserId(int userId) {
        log.debug("Retrieving all books in the wishlist associated with Patron ID: " + userId);
        Session session = sessionFactory.getCurrentSession();
        Query q = session.getNamedQuery("getWishlistBookByPatronId");
        q.setParameter("patronid",userId);
        List<WishlistBook> wishlistBooks = q.getResultList();
        if(wishlistBooks.isEmpty()) {
            log.warn("Failed to retrieve the books in the wishlist!");
            return new ArrayList<WishlistBook>();
        }
        else {
            log.info("Successfully retrieved all books in the wishlist.");
            return wishlistBooks;
        }
    }

    /**
     * <h4>deleteWishlistBook</h4>
     * <p>Deletes a book from a wishlist belonging to a particular patron (userId).</p>
     * @param userId The patron tied to the wishlist
     * @param isbn The book to be deleted from the table
     * @return A boolean value based on whether the table was successfully affected or not
     */
    public boolean deleteWishlistBook(int userId, String isbn) {
        log.debug("Deleting book with ISBN: " + isbn + ", from the wishlist associated with Patron ID: " + userId);
        Session session = sessionFactory.getCurrentSession();
        Query q = session.getNamedNativeQuery("deleteWishlistBookByPatronIdAndIsbn");
        q.setParameter("patronid",userId);
        q.setParameter("isbn",isbn);
        int rowsAffected = q.executeUpdate();
        if(rowsAffected > 0) {
            log.info("Successfully deleted the book: " + isbn + " from the wishlist.");
            return true;
        } else {
            log.warn("Failed to delete the book from the wishlist.");
            return false;
        }
    }

    /**
     * <h4>deleteWishlistBook</h4>
     * <p>Delete all books from a wishlist belonging to a particular patron (userId).</p>
     * @param userId The patron tied to the wishlist
     * @return A boolean value based on whether the table was successfully affected or not
     */
    public boolean deleteWishlistBook(int userId) {
        log.debug("Deleting all books from the wishlist associated with Patron ID: " + userId);
        Session session = sessionFactory.getCurrentSession();
        Query q = session.getNamedNativeQuery("deleteWishlistBookByPatronId");
        q.setParameter("patronid",userId);
        int rowsAffected = q.executeUpdate();
        if(rowsAffected > 0) {
            log.info("Successfully deleted all books from the patron's wishlist.");
            return true;
        } else {
            log.warn("Failed to delete the book from the wishlist.");
            return false;
        }
    }

}


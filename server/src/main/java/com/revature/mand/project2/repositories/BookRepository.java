package com.revature.mand.project2.repositories;

import com.revature.mand.project2.entities.Book;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *<h3>BookRepository</h3>
 * <p>Repository layer class defining several methods for executing
 *    CRUD functions on the book table.</p>
 *
 * @author Mason Underhill
 */
@Repository
@Log4j2
public class BookRepository {

    SessionFactory sessionFactory;

    @Autowired
    BookRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * <h4>getByIsbn</h4>
     * <p>Retrieves all books from a cart belonging to a particular patron (userId).</p>
     * @param UserId The patron tied to the cart
     * @return A list of all retrieved books.
     */
    public Book getByIsbn(String isbn) {
        log.debug("Retrieving all book in book table with isbn: " + isbn);
        Session session = sessionFactory.getCurrentSession();
        Query q = session.getNamedQuery("getBookByIsbn");
        q.setParameter("isbn",isbn);
        List<Book> book = q.getResultList();
        if(book.isEmpty())
         {
             log.warn("Could not find any books with the ISBN: " + isbn);
             return null;
         } else 
         {
             log.info("Book found! Returning book");
             return book.get(0);
         }
    }
}

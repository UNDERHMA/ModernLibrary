package com.revature.mand.project2.repositories;

import com.revature.mand.project2.entities.Patron;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *<h3>PatronRepository</h3>
 * <p>Repository layer class defining several methods for executing
 *    CRUD functions on the patron table.</p>
 *
 * @author Mason Underhill
 */
@Repository
@Log4j2
public class PatronRepository {

    SessionFactory sessionFactory;

    @Autowired
    PatronRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * <h4>getById</h4>
     * <p>Retrieves a patron from the database by a given id</p>
     * @param id The id of the patron being retrieved
     * @return A patron from the table
     */
    public Patron getById(int id) {
        log.debug("Retrieving patron by [ID]: " + id);
        Session session = sessionFactory.getCurrentSession();
        return (Patron) session.get(Patron.class, id);
    }

    /**
     * <h4>getByUserName</h4>
     * <p>Retrieves a patron from the database by a given username</p>
     * @param username The username of the patron being retrieved
     * @return A patron from the table
     */
    public Patron getByUserName(String username) {
        log.debug("Retrieving patron by [username]: " + username);
        Session session = sessionFactory.getCurrentSession();
        Query q = session.getNamedQuery("getPatronByUserName");
        q.setParameter("username",username);
        List patron = q.getResultList();
        if(patron.isEmpty()) {
            log.warn("Failed to retrieve a patron by that username.");
            return null;
        }
        else {
            log.info("Successfully retrieved the following patron: " + patron.get(0).toString());
            return (Patron) patron.get(0);
        }
    }

    /**
     * <h4>save</h4>
     * <p>Saves patron to the patron table.</p>
     * @param patron The patron to be persisted to the database
     * @return A boolean representing whether the patron was successfully persisted.
     */
    public boolean save(Patron patron) {
        log.debug("Saving patron: " + patron.getFirstName() + " " + patron.getLastName() + ", to database...");
        Session session = sessionFactory.getCurrentSession();
        session.save(patron);
        return true;
    }

}

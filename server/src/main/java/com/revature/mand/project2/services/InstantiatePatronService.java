package com.revature.mand.project2.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.mand.project2.entities.Book;
import com.revature.mand.project2.entities.Patron;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * <h3>InstantiatePatronService</h3>
 * <p>Service layer class responsible for mapping JSON patron representations to a Patron POJO</p>
 * @author Mason Underhill
 */
@Log4j2
@Service
public class InstantiatePatronService {

    /**
     * <h4>instantiatepatron</h4>
     * <p>Maps a single JSON patron into a patron model POJO.</p>
     * @param jsonString JSON string containing information for a single patron
     * @return The Patron model object
     */
    public Patron instantiatePatron(String jsonString) {
        log.debug("Initializing Patron object from JSON...");
        ObjectMapper om = new ObjectMapper();
        Patron patron = null;
        try {
            patron = om.readValue(jsonString, Patron.class);
        }catch (JsonProcessingException e) {
            log.error("Failed to interpret JSON string into a Patron object!", e);
        }
        log.info("Returning the following patron: " + patron.getFirstName() + " " + patron.getLastName() + ".");
        return patron;
    }
}

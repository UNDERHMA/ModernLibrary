package com.revature.mand.project2.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.mand.project2.entities.Patron;
import com.revature.mand.project2.repositories.PatronRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <h3>UserService</h3>
 * <p>Service layer class responsible for retrieving a patron
 *    from the database and returning a JSON representation.</p>
 * @author Mason Underhill
 */
@Log4j2
@Service
public class UserService {

    PatronRepository patronRepository;

    @Autowired
    public UserService(PatronRepository patronRepository) {
        this.patronRepository = patronRepository;
    }

    /**
     * <h4>getUserByUserName</h4>
     * <p>Retrieves patron from the database and converts it into a JSON object which is then returned.</p>
     * @param username Username associated with the patron we want to retrieve from the database.
     * @return The JSON representation of the returned Patron
     */
    @Transactional(readOnly = true)
    public String getUserByUserName(String username) {
        log.debug("Retrieving patron from the database with username: " + username);
        Patron patron = patronRepository.getByUserName(username);
        ObjectMapper om = new ObjectMapper();
        String patronString = "";
        if(patron != null) {
            try {
                patronString = om.writeValueAsString(patron);
            }catch (JsonProcessingException e) {
                log.error("Failed to convert Patron object to a JSON String!", e);
            }
            patronString = patronString.replaceFirst("registrationDate\":.+}",
                                                     "registrationDate\":\""+patron.dateAsString()+"\"}");
            log.debug("Returning the resulting JSON: " + patronString);
        }
        return patronString;
    }
}

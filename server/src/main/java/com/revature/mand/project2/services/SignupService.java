package com.revature.mand.project2.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.mand.project2.entities.Patron;
import com.revature.mand.project2.repositories.PatronRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <h3>SignupService</h3>
 * <p>Service layer class responsible for processing the persistence of a newly registered patron.</p>
 * @author Mason Underhill
 */
@Log4j2
@Service
public class SignupService {

    PatronRepository patronRepository;

    @Autowired
    SignupService(PatronRepository patronRepository) {
        this.patronRepository = patronRepository;
    }

    /**
     * <h4>registerUser</h4>
     * <p>Maps a JSON representation of a new patron to a Patron</p>
     * @param jsonString JSON representation of a Patron
     * @return A boolean
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public boolean registerUser(String jsonString) {
        log.debug("Registering user: " + jsonString);
        ObjectMapper om = new ObjectMapper();
        Patron patron = null;
        try {
            patron = om.readValue(jsonString, Patron.class);
        }catch (JsonProcessingException e) {
            log.error("Failed to interpret JSON String into Patron!", e);
        }
        if(patron != null) {
            log.info("Saving new user to patron database.");
            // do not allow signup if user exists
            if(patronRepository.getByUserName(patron.getUsername()) != null) {
                return false;
            }
            return patronRepository.save(patron);
        } else {
            log.error("JSON string unable to be converted into Patron object.");
            return false;
        }
    }
}

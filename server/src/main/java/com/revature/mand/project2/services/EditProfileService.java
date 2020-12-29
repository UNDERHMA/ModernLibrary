package com.revature.mand.project2.services;

import com.revature.mand.project2.entities.Patron;
import com.revature.mand.project2.repositories.PatronRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <h3>EditProfileService</h3>
 * <p>Service layer class which processes change requests for a patron's information.</p>
 * @author Mason Underhill
 */
@Log4j2
@Service
public class EditProfileService {

    PatronRepository patronRepository;
    InstantiatePatronService instantiatePatronService;

    @Autowired
    EditProfileService(PatronRepository patronRepository,
                       InstantiatePatronService instantiatePatronService) {
        this.patronRepository = patronRepository;
        this.instantiatePatronService = instantiatePatronService;
    }

    /**
     * <h3>editProfile</h3>
     * <p>Takes in a patron with edited information and replaces the
     *    patron with the associated userId in the patron table.</p>
     * @param jsonString JSON representation of the patron to be changed
     * @param userId userId associated with the patron being edited (used for database comparison)
     * @return
     */
    @Transactional(isolation= Isolation.SERIALIZABLE)
    public boolean editProfile(String jsonString, int userId) {
        log.debug("Editing a patron using JSON data...");
        Patron patronUpdates = instantiatePatronService.instantiatePatron(jsonString);
        if(patronUpdates == null) {
            log.error("Patron profile was unable to be instantiated using that JSON data!");
            return false;
        }
        Patron patronOld = patronRepository.getById(userId);
        if(patronOld != null & patronUpdates != null) {
            patronOld.setFirstName(patronUpdates.getFirstName());
            patronOld.setLastName(patronUpdates.getLastName());
            patronOld.setUsername(patronUpdates.getUsername());
            patronOld.setPassHash(patronUpdates.getPassHash());
            patronOld.setEmail(patronUpdates.getEmail());
            patronOld.setPhone(patronUpdates.getPhone());
            log.debug("Successfully updated patron info...");
            return true;
        }
        log.error("Failed to update the patron using JSON data!");
        return false;
    }
}

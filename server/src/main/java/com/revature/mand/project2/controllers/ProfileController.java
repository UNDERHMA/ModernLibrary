package com.revature.mand.project2.controllers;

import com.revature.mand.project2.services.EditProfileService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * <h3>ProfileController</h3>
 * <p>Controller layer class used to process requests from the front end
 *    and deliver the appropriate data from the database relating to
 *    a patron's profile.</p>
 *
 * @author Mason Underhill
 */
@RestController
@Log4j2
@RequestMapping("update-profile")
public class ProfileController {

    private EditProfileService editProfileService;

    @Autowired
    public ProfileController(EditProfileService editProfileService) {
        this.editProfileService = editProfileService;
    }

    /**
     * <h4>addWishlistBook</h4>
     * <p>Add a book to the wishlist table item associated with a particular patron</p>
     * @param jsonString JSON object representing a book to add to the wishlist
     * @param userId The patron who owns the wishlist
     * @return An HTTP response
     */
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> editProfile(@RequestBody String jsonString,
                                                  @RequestParam int userId) {
        log.debug("Changing patron: " + userId + " with the following information: " + jsonString);
        boolean result = editProfileService.editProfile(jsonString, userId);
        if(result) {
            log.info("Successfully updated the patron information!");
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            log.error("Failed to update the patron information!");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

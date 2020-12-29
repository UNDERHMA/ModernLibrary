package com.revature.mand.project2.controllers;

import com.revature.mand.project2.services.SignupService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * <h3>Book Controller</h3>
 * <p>Controller layer class used to process requests from the front end
 *    and deliver the appropriate data from the database relating to
 *    a patron's signup data.</p>
 *
 * @author Mason Underhill
 */
@RestController
@Log4j2
@RequestMapping("signup")
public class SignupController {

    private SignupService signupService;

    @Autowired
    public SignupController(SignupService signupService) {
        this.signupService = signupService;
    }

    /**
     * <h4>registerUser</h4>
     * <p>Take in a JSON object representing a user to be persisted through the user table</p>
     * @param jsonString The JSON object representing a new patron
     * @return An HTTP response
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> registerUser(@RequestBody String jsonString) {
        log.debug("Creating a new patron in the database: " + jsonString);
        boolean result = signupService.registerUser(jsonString);
        if(result) {
            log.info("Successfully created a new patron in the database!");
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            log.error("Failed to create a new patron!");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}

package com.revature.mand.project2.controllers;

import com.revature.mand.project2.services.UserService;
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
 *    a patron.</p>
 *
 * @author Mason Underhill
 */
@RestController
@Log4j2
@RequestMapping("get-user")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * <h4>getUsersByUserName</h4>
     * <p>Retrieve the user data from the patron table by a username</p>
     * @param username The username tied to the patron table
     * @return An HTTP response including a patron object
     */
    @GetMapping(produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> getUserByUserName(@RequestParam String username) {
        log.debug("Retrieving patron by the following username: " + username);
        String user = userService.getUserByUserName(username);
        if(user.length() > 0) {
            log.info("Successfully retrieved a patron from the database!");
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            log.warn("Failed to retrieve a patron from the database!");
            return new ResponseEntity<>(user, HttpStatus.UNAUTHORIZED);
        }
    }
}

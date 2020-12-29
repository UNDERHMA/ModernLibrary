package com.revature.mand.project2.controllers;

import com.revature.mand.project2.services.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * <h3>ReservedBookController</h3>
 * <p>Controller layer class used to process requests from the front end
 *    and deliver the appropriate data from the database relating to
 *    a library book.</p>
 *
 * @author Mason Underhill
 */
@RestController
@Log4j2
@RequestMapping("reserved")
public class ReservedBookController {

    private AddReservedBookService addReservedBookService;
    private RetrieveReservedBookService retrieveReservedBookService;
    private RetrieveReturnedBookService retrieveReturnedBookService;
    private ReturnReservedBookService returnReservedBookService;

    @Autowired
    public ReservedBookController(AddReservedBookService addReservedBookService
            , RetrieveReservedBookService retrieveReservedBookService
            , RetrieveReturnedBookService retrieveReturnedBookService
            , ReturnReservedBookService returnReservedBookService) {
        this.addReservedBookService = addReservedBookService;
        this.retrieveReservedBookService = retrieveReservedBookService;
        this.retrieveReturnedBookService = retrieveReturnedBookService;
        this.returnReservedBookService = returnReservedBookService;
    }

    /**
     * <h4>addReservedBooks</h4>
     * <p>Add a book to the reserved table belonging to a particular patron (userId).</p>
     * @param jsonString The JSON object representing the book to be persisted
     * @param userId The patron tied to the reservation
     * @return An HTTP response
     */
    @PostMapping(path="add-all-reserved",consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> addReservedBooks(@RequestBody String jsonString,
                                                  @RequestParam int userId) {
        log.debug("Processing the following book into patron reservation: " + jsonString + ".");
        String result = addReservedBookService.addReservedBooks(jsonString, userId);
        if(result.equals("Your books have been reserved")) {
            log.info("Successfully added books to the reservation.");
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            log.error("Failed to add books to the reservation.");
            return new ResponseEntity<>(result,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * <h4>getReservedBookById</h4>
     * <p>Retrieve all of the books from a reservation belonging to a particular patron (userId).</p>
     * @param userId The patron
     * @return An HTTP response including the list of all books in a patron's reservation
     */
    @GetMapping(path="get-reserved",produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> getReservedBookById(@RequestParam int userId) {
        log.debug("Retrieving all books from the reservation tied to the following patron: " + userId);
        String reservedBookString = retrieveReservedBookService.getReservedBooks(userId);
        if(reservedBookString.length() > 0) {
            log.info("Successfully retrieved books from the reservation.");
            return new ResponseEntity<>(reservedBookString, HttpStatus.OK);
        } else {
            log.warn("Failed to retrieve books from the reservation.");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * <h4>getReturnedBookById</h4>
     * <p>Retrieve all of the returned books belonging to a particular patron (userId).</p>
     * @param userId The patron
     * @return An HTTP response including the list of all books in a patron's reservation
     */
    @GetMapping(path="get-returned",produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> getReturnedBookById(@RequestParam int userId) {
        log.debug("Retrieving all returned books from the reservation tied to the following patron: " + userId);
        String returnedBookString = retrieveReturnedBookService.getReturnedBooks(userId);
        if(returnedBookString.length() > 0) {
            log.info("Successfully retrieved returned books from the reservation.");
            return new ResponseEntity<>(returnedBookString, HttpStatus.OK);
        } else {
            log.warn("Failed to retrieve returned books from the reservation.");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * <h4>deleteReservedBookByUserIdAndIsbn</h4>
     * <p>Removes a book from a patron's reservations by its ISBN number</p>
     *
     * @param userId The patron tied to the reservation
     * @param isbn The ISBN of the book being dropped from the reservation
     * @return The HTTP response
     */
    @DeleteMapping(path="delete-from-reserved")
    @ResponseBody
    public ResponseEntity<String> returnReservedBookByUserIdAndIsbn(@RequestParam int userId,
                                                            @RequestParam String isbn) {
        log.debug("Returning the following book (ISBN): " + isbn + " from the reservation tied to the following patron: " + userId);
        String result = returnReservedBookService.returnReservedBook(userId, isbn);
        if(result.equals("Your book has been returned")) {
            log.info("Successfully returned the book from the reservation.");
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            log.error("Failed to return the book from the reservation.");
            return new ResponseEntity<>(result,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * <h4>deleteReservedBookByUserId</h4>
     * <p>Remove all books from a cart belonging to a particular patron (userId).</p>
     *
     * @param userId The patron that owns the cart
     * @return The HTTP response
     */
    @DeleteMapping(path="delete-all-from-reserved")
    @ResponseBody
    public ResponseEntity<String> returnReservedBookByUserId(@RequestParam int userId) {
        log.debug("Returning all books from the reservation tied to the following patron: " + userId);
        String result = returnReservedBookService.returnReservedBooks(userId);
        if(result.equals("All books have been returned")) {
            log.info("Successfully returned all books from the reservation.");
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            log.error("Failed to return books from the reservation.");
            return new ResponseEntity<>(result,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

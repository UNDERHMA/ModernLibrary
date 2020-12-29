package com.revature.mand.project2.controllers;

import com.revature.mand.project2.services.*;
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
 *    a patron's wishlist.</p>
 *
 * @author Mason Underhill
 */
@RestController
@Log4j2
@RequestMapping("wishlist")
public class WishlistController {

    private AddWishlistBookService addWishlistBookService;
    private RetrieveWishlistBookService retrieveWishlistBookService;
    private DeleteWishlistBookService deleteWishlistBookService;

    @Autowired
    public WishlistController( AddWishlistBookService addWishlistBookService
            ,RetrieveWishlistBookService retrieveWishlistBookService
            ,DeleteWishlistBookService deleteWishlistBookService) {
        this.addWishlistBookService = addWishlistBookService;
        this.retrieveWishlistBookService = retrieveWishlistBookService;
        this.deleteWishlistBookService = deleteWishlistBookService;
    }

    /**
     * <h4>addWishlistBook</h4>
     * <p>Adds a book to the wishlist table and sends a 200 status back to the front-end</p>
     *
     * @param jsonString The JSON representation of the book being persisted in the wishlist table
     * @param userId The patron that owns the wishlist to which the book is being added
     * @return An HTTP response
     */
    @PostMapping(path="add-wish-list",consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> addWishlistBook(@RequestBody String jsonString,
                                                  @RequestParam int userId) {
        log.debug("Processing the following book into patron's wishlist: " + jsonString + ".");
        String result = addWishlistBookService.addWishlistBook(jsonString, userId);
        if(result.equals("Successfully added to Wishlist")) {
            log.info("Successfully added books to the wishlist.");
            return new ResponseEntity<>(result,HttpStatus.OK);
        } else if (result.equals("There was an error processing your request.")) {
            return new ResponseEntity<>(result,HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            log.debug("This book is already in your wishlist.");
            return new ResponseEntity<>(result,HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * <h4>getWishlistBookById</h4>
     * <p>Retrieves all of the books in a wishlist belong to a particular patron (userId).</p>
     *
     * @param userId The patron that owns the wishlist
     * @return An HTTP response
     */
    @GetMapping(path="get-wishlist",produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> getWishlistBookById(@RequestParam int userId) {
        log.debug("Retrieving all books from the wishlist tied to the following patron: " + userId);
        String wishlistBookString = retrieveWishlistBookService.getWishlistBookById(userId);
        if(wishlistBookString.length() > 0) {
            log.info("Successfully retrieved books from the wishlist.");
            return new ResponseEntity<>(wishlistBookString, HttpStatus.OK);
        } else {
            log.warn("Failed to retrieve books from the wishlist.");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * <h4>deleteWishlistBookByUserIdAndIsbn</h4>
     * <p>Removes a book from a patron's wishlist by its ISBN number</p>
     *
     * @param userId The patron that owns the wishlist
     * @param isbn The ISBN of the book being dropped from the wishlist
     * @return The HTTP response
     */
    @DeleteMapping(path="delete-from-wishlist")
    @ResponseBody
    public ResponseEntity<String> deleteWishlistBookByUserIdAndIsbn(@RequestParam int userId,
                                                                    @RequestParam String isbn) {
        log.debug("Deleting the following book (ISBN): " + isbn + " from the wishlist tied to the following patron: " + userId);
        String result = deleteWishlistBookService.deleteWishlistBook(userId, isbn);
        if(result.equals("Successfully removed book from your wishlist.")) {
            log.info("Successfully deleted the book from the wishlist.");
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            log.warn("Failed to delete the book from the wishlist.");
            return new ResponseEntity<>(result,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * <h4>deleteWishlistBookByUserId</h4>
     * <p>Remove all books from a wishlist belonging to a particular patron (userId).</p>
     *
     * @param userId The patron that owns the wishlist
     * @return The HTTP response
     */
    @DeleteMapping(path="delete-all-from-wishlist")
    @ResponseBody
    public ResponseEntity<String> deleteWishlistBookByUserId(@RequestParam int userId) {
        log.debug("Deleting all books from the wishlist tied to the following patron: " + userId);
        String result = deleteWishlistBookService.deleteWishlistBook(userId);
        if(result.equals("Successfully removed all books from your wishlist.")) {
            log.info("Successfully deleted books from the wishlist.");
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            log.warn("Failed to delete books from the wishlist.");
            return new ResponseEntity<>(result,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

package com.revature.mand.project2.controllers;

import com.revature.mand.project2.services.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * <h3>CartController</h3>
 * <p>Controller layer class used to process requests from the front end
 *    and deliver the appropriate data from the database relating to
 *    a patron's checkout cart.</p>
 *
 * @author Mason Underhill
 */
@RestController
@Log4j2
@RequestMapping("cart")
public class CartController {

    private AddBookToCartService addBookToCartService;
    private RetrieveCartBookService retrieveCartBookService;
    private DeleteCartBookService deleteCartBookService;

    @Autowired
    public CartController(AddBookToCartService addBookToCartService
            ,RetrieveCartBookService retrieveCartBookService
            ,DeleteCartBookService deleteCartBookService) {
        this.addBookToCartService = addBookToCartService;
        this.retrieveCartBookService = retrieveCartBookService;
        this.deleteCartBookService = deleteCartBookService;
    }

    /**
     * <h4>addCartBook</h4>
     * <p>Add a book to the cart table belonging to a particular patron (userId).</p>
     * @param jsonString The JSON object representing the book to be persisted
     * @param userId The patron that owns the cart
     * @return An HTTP response
     */
    @PostMapping(path="add-cart",consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> addCartBook(@RequestBody String jsonString,
                                                  @RequestParam int userId) {
        log.debug("Processing the following book into shopping cart: " + jsonString + ".");
        String result = addBookToCartService.addCartBook(jsonString, userId);
        if(result.equals("Successfully added book to cart")) {
            log.info("Successfully added books to the shopping cart.");
            return new ResponseEntity<>(result,HttpStatus.OK);
        } else if (result.equals("There was an error processing your request.")) {
            return new ResponseEntity<>(result,HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            log.debug("You are not eligible to reserve this book because you have reserved it in the past 90 days.");
            return new ResponseEntity<>(result,HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * <h4>getCartBookById</h4>
     * <p>Retrieve all of the books from a cart belonging to a particular patron (userId).</p>
     * @param userId The patron
     * @return An HTTP response including the list of all books in a patron's cart
     */
    @GetMapping(path="get-cart",produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> getCartBookById(@RequestParam int userId) {
        log.debug("Retrieving all books from the shopping cart tied to the following patron: " + userId);
        String reservedBookString = retrieveCartBookService.getCartBookById(userId);
        if(reservedBookString.length() > 0) {
            log.info("Successfully retrieved books from the shopping cart.");
            return new ResponseEntity<>(reservedBookString,HttpStatus.OK);
        }else {
            log.warn("Failed to retrieve books from the shopping cart.");
            return new ResponseEntity<>(reservedBookString,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * <h4>deleteCartBookByUserIdAndIsbn</h4>
     * <p>Removes a book from a patron's cart by its ISBN number</p>
     *
     * @param userId The patron that owns the cart
     * @param isbn The ISBN of the book being dropped from the cart
     * @return The HTTP response
     */
    @DeleteMapping(path="delete-from-cart")
    public ResponseEntity<String> deleteCartBookByUserIdAndIsbn(@RequestParam int userId,
                                                                    @RequestParam String isbn) {
        log.debug("Deleting the following book (ISBN): " + isbn + " from the shopping cart tied to the following patron: " + userId);
        String result = deleteCartBookService.deleteCartBook(userId, isbn);
        if(result.equals("Book has been removed from cart.")) {
            log.info("Successfully deleted the book from the shopping cart.");
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            log.error("Failed to delete the book from the shopping cart.");
            return new ResponseEntity<>(result,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * <h4>deleteCartBookByUserId</h4>
     * <p>Remove all books from a cart belonging to a particular patron (userId).</p>
     *
     * @param userId The patron that owns the cart
     * @return The HTTP response
     */
    @DeleteMapping(path="delete-all-from-cart")
    @ResponseBody
    public ResponseEntity<String> deleteCartBookByUserId(@RequestParam int userId) {
        log.debug("Deleting all books from the shopping cart tied to the following patron: " + userId);
        String result = deleteCartBookService.deleteCartBook(userId);
        if (result.equals("All books have been removed from cart.")) {
            log.info("Successfully deleted books from the shopping cart.");
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            log.error("Failed to delete books from the shopping cart.");
            return new ResponseEntity<>(result,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

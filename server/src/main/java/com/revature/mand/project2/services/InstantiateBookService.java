package com.revature.mand.project2.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.mand.project2.entities.Book;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * <h3>InstantiateBookService</h3>
 * <p>Service layer class responsible for mapping JSON book representations to Book POJOs.</p>
 * @author Mason Underhill
 */
@Log4j2
@Service
public class InstantiateBookService {

    /**
     * <h4>instantiateBook</h4>
     * <p>Maps a single JSON book into a book model POJO.</p>
     * @param jsonString JSON string containing information for a single book
     * @return The Book model object
     */
    public Book instantiateBook(String jsonString) {
        log.debug("Initializing Book object from JSON...");
        ObjectMapper om = new ObjectMapper();
        Book book = null;
        try {
            book = om.readValue(jsonString, Book.class);
        }catch (JsonProcessingException e) {
            log.error("Failed to interpret JSON string into a Book object!", e);
        }
        log.info("Returning the following book: " + book.getTitle());
        return book;
    }

    /**
     * <h4>instantiateBookArray</h4>
     * <p>Maps a JSON representation of a list of books into a list of book model POJOs.</p>
     * @param jsonString JSON string containing information for a list of books
     * @return A List of Book model objects
     */
    public Book[] instantiateBookArray(String jsonString) {
        log.debug("Initializing Book array from JSON...");
        ObjectMapper om = new ObjectMapper();
        Book[] books = null;
        try {
            books = om.readValue(jsonString, Book[].class);
        }catch (JsonProcessingException e) {
            log.error("Failed to interpret JSON string into a Book array!", e);
        }
        for(Book book : books) {
            log.info("Returning the following book: " + book.getTitle());
        }
        return books;
    }
}

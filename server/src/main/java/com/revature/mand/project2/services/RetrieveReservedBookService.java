package com.revature.mand.project2.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.mand.project2.entities.ReservedBook;
import com.revature.mand.project2.repositories.ReservedBookRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <h3>RetrieveReservedBookService</h3>
 * <p>Service layer class responsible for mapping database rows into JSON strings.</p>
 * @author Mason Underhill
 */
@Log4j2
@Service
public class RetrieveReservedBookService {

    ReservedBookRepository reservedBookRepository;

    @Autowired
    RetrieveReservedBookService(ReservedBookRepository reservedBookRepository) {
        this.reservedBookRepository = reservedBookRepository;
    }

    /**
     * <h4>getReservedBooks</h4>
     * <p>Arranges all books from a reservation belonging to a particular patron into a JSON string.</p>
     * @param userId The Patron id tied to the reservation
     * @return The JSON string of books
     */
    @Transactional(readOnly = true)
    public String getReservedBooks(int userId) {
        log.debug("Retrieving books in the reservation tied to the patron: " + userId);
        List<ReservedBook> reservedBooks = reservedBookRepository.getReservedBooks(userId);
        StringBuilder reservedBooksString = new StringBuilder();
        reservedBooksString.append("[");
        if(!reservedBooks.isEmpty()) {
                        log.info("Successfully retrieved books from the reservation. Converting into JSON...");
            for (int i = 0; i < reservedBooks.size(); i++) {
                ObjectMapper om = new ObjectMapper();
                String bookString = "";
                try {
                    ReservedBook rb = reservedBooks.get(i);
                    bookString = om.writeValueAsString(rb);
                    // Converting to readable dates
                    bookString = bookString.replaceFirst("reservationDate\":\\d+",
                            "reservationDate\":\""+rb.dateAsString(rb.getReservationDate())+"\"");
                    bookString = bookString.replaceFirst("dueDate\":\\d+",
                            "dueDate\":\""+rb.dateAsString(rb.getDueDate())+"\"");
                    bookString = bookString.replaceFirst("returnDate\":\\d+",
                            "returnDate\":\""+rb.dateAsString(rb.getReturnDate())+"\"");
                    // saving Book string to JSON array of Book Objects
                    reservedBooksString.append(bookString);
                    if(i < reservedBooks.size()-1) {
                        reservedBooksString.append(",");
                    }
                } catch (JsonProcessingException e) {
                    log.error("Unable to convert reservation books into JSON!", e);
                }
            }
            reservedBooksString.append("]");
            log.debug("JSON String result: " + reservedBooksString);
            return reservedBooksString.toString();
        }
        else {
            reservedBooksString.append("]");
            log.info("No books found in the reservation! Returning blank JSON string.");
            return reservedBooksString.toString();
        }
    }
}

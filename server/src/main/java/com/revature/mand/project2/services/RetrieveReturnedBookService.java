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

@Log4j2
@Service
public class RetrieveReturnedBookService {

    ReservedBookRepository reservedBookRepository;

    @Autowired
    RetrieveReturnedBookService(ReservedBookRepository reservedBookRepository) {
        this.reservedBookRepository = reservedBookRepository;
    }

    /**
     * <h4>getReturnedBooks</h4>
     * <p>Retrieves all returned books belonging to a particular patron into a JSON string.</p>
     * @param userId The Patron id tied to the reservation
     * @return The JSON string of books
     */
    @Transactional(readOnly = true)
    public String getReturnedBooks(int userId) {
        log.debug("Retrieving returned books in the reservation tied to the patron: " + userId);
        List<ReservedBook> returnedBooks = reservedBookRepository.getReturnedBooks(userId);
        StringBuilder returnedBooksString = new StringBuilder();
        returnedBooksString.append("[");
        if(!returnedBooks.isEmpty()) {
            log.info("Successfully retrieved returned books from the reservation. Converting into JSON...");
            for (int i = 0; i < returnedBooks.size(); i++) {
                ObjectMapper om = new ObjectMapper();
                String bookString = "";
                try {
                    ReservedBook rb = returnedBooks.get(i);
                    bookString = om.writeValueAsString(rb);
                    // Converting to readable dates
                    bookString = bookString.replaceFirst("reservationDate\":\\d+",
                            "reservationDate\":\""+rb.dateAsString(rb.getReservationDate())+"\"");
                    bookString = bookString.replaceFirst("dueDate\":\\d+",
                            "dueDate\":\""+rb.dateAsString(rb.getDueDate())+"\"");
                    bookString = bookString.replaceFirst("returnDate\":\\d+",
                            "returnDate\":\""+rb.dateAsString(rb.getReturnDate())+"\"");
                    // saving Book string to JSON array of Book Objects
                    returnedBooksString.append(bookString);
                    if(i < returnedBooks.size()-1) {
                        returnedBooksString.append(",");
                    }
                } catch (JsonProcessingException e) {
                    log.error("Unable to convert returned books into JSON!", e);
                }
            }
            returnedBooksString.append("]");
            log.debug("JSON String result: " + returnedBooksString);
            return returnedBooksString.toString();
        }
        else {
            returnedBooksString.append("]");
            log.info("No returned books found in the reservation! Returning blank JSON string.");
            return returnedBooksString.toString();
        }
    }
}

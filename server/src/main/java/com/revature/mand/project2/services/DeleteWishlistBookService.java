package com.revature.mand.project2.services;

import com.revature.mand.project2.repositories.WishlistBookRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <h3>DeleteWishlistBookService</h3>
 * <p>Service layer class which can be called to process a book to be dropped to a patron's wishlist.</p>
 * @author Mason Underhill
 */
@Log4j2
@Service
public class DeleteWishlistBookService {

    WishlistBookRepository wishlistBookRepository;

    @Autowired
    DeleteWishlistBookService(WishlistBookRepository wishlistBookRepository) {
        this.wishlistBookRepository = wishlistBookRepository;
    }

    /**
     * <h4>deleteWishlistBook</h4>
     * <p>Drops a book from a wishlist belonging to a particular patron.</p>
     * @param userId The patron tied to the reservation
     * @param isbn The book to be dropped from the reservation
     * @return A boolean
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public String deleteWishlistBook(int userId, String isbn) {
        log.debug("Deleting the following book: " + isbn +", from the patron's (" + userId +") wishlist!");
        if(wishlistBookRepository.deleteWishlistBook(userId,isbn)) {
            return "Successfully removed book from your wishlist.";
        } else{
            return "An error occurred removing book from your wishlist.";
        }
    }

    /**
     * <h4>deleteWishlistBook</h4>
     * <p>Drops all books from a wishlist belonging to a particular patron.</p>
     * @param userId The patron tied to the wishlist
     * @return A boolean
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public String deleteWishlistBook(int userId) {
        log.debug("Deleting the all book from the patron's (" + userId +") wishlist!");
        if(wishlistBookRepository.deleteWishlistBook(userId)){
            return "Successfully removed all books from your wishlist.";
        } else{
            return "An error occurred removing all books from your wishlist.";
        }
    }
}

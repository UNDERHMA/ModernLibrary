package com.revature.mand.project2.entities;

import lombok.Data;

import javax.persistence.*;

/**
 * <h3>Wishlist Model</h3>
 * <p> Models all of the attributes of a book in a patron's wishlist.</p>
 *
 * @author Javier Gonzalez & Mason Underhill
 */
@NamedQueries(
        value={
                @NamedQuery(name="getWishlistBookByPatronId", query="from WishlistBook " +
                        "where patron_id = :patronid"),
                @NamedQuery(name="getWishlistBookByPatronIdAndIsbn", query="from WishlistBook " +
                        "where patron_id = :patronid and isbn = :isbn")
        }
)
@NamedNativeQueries(
        value={
                @NamedNativeQuery(name="deleteWishlistBookByPatronIdAndIsbn",
                        query="Delete FROM wishlist " +
                                "WHERE patron_id = :patronid AND isbn = :isbn"),
                @NamedNativeQuery(name="deleteWishlistBookByPatronId",
                        query="Delete FROM wishlist " +
                                "WHERE patron_id = :patronid")
        }
)
@Entity
@Table(name = "wishlist")
@Data
public class WishlistBook {

    //COLUMN FIELDS
    @Id
    @Column(name="wishlist_id", columnDefinition="serial primary key")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int wishlistId;

    @Column(name = "patron_id")
    private int patronId;

    @ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinColumn(name="isbn",
            referencedColumnName="book_isbn",
            columnDefinition = "INT")
    private Book book;

    @Column(name = "rank_number")
    private int rank = 0; // placeholder value until we implement this

    @Column(name = "email_notify")
    private boolean willNotify = false; // placeholder value until we implement this

}

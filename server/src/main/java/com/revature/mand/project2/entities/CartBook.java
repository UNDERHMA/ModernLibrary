package com.revature.mand.project2.entities;

import lombok.Data;

import javax.persistence.*;

/**
 * <h3>Cart Model</h3>
 * <p> Models all of the attributes of a book in a patron's checkout cart.</p>
 *
 * @author Javier Gonzalez & Mason Underhill
 */
@NamedQueries(
        value={
                @NamedQuery(name="getCartBookByPatronId", query="from CartBook " +
                        "where patron_id = :patronid"),
                @NamedQuery(name="getCartBookByPatronIdAndIsbn", query="from CartBook " +
                        "where patron_id = :patronid and isbn = :isbn")
        }
)
@NamedNativeQueries(
        value={
                @NamedNativeQuery(name="deleteCartBookByPatronIdAndIsbn",
                        query="Delete FROM cart " +
                                "WHERE patron_id = :patronid AND isbn = :isbn"),
                @NamedNativeQuery(name="deleteCartBookByPatronId",
                        query="Delete FROM cart " +
                                "WHERE patron_id = :patronid")
        }
)
@Entity
@Table(name = "cart")
@Data
public class CartBook {

    //COLUMN FIELDS
    @Id
    @Column(name="cart_id", columnDefinition="serial primary key")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cartId;

    @Column(name = "patron_id")
    private int patronId;

    @ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinColumn(name="isbn",
            referencedColumnName="book_isbn",
            columnDefinition = "INT")
    private Book book;

    @Column(name = "to_checkout")
    private boolean toCheckout;
}

package com.revature.mand.project2.entities;

import lombok.Data;

import javax.persistence.*;

/**
 * <h3>Book Model</h3>
 * <p> Models all of the attributes of a book in our library.</p>
 *
 * @author Javier Gonzalez
 */
@NamedQueries(
        value={
                @NamedQuery(name="getBookByIsbn", query="from Book " +
                        "where book_isbn LIKE :isbn")
        }
)
@Entity
@Table(name = "book")
@Data
public class Book {

    @Id
    @Column(name="book_isbn")
    private String isbn;

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "image_url")
    private String image;

    @Column(name= "description")
    private String description;

    @Column(name= "copies_checked_out")
    private int copiesCheckedOut;
}
















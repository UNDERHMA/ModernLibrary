package com.revature.mand.project2.entities;

import lombok.Data;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <h3>Patron Model</h3>
 * <p> Models all of the attributes of a library patron.</p>
 *
 * @author Javier Gonzalez & Mason Underhill
 */
@NamedQueries(
        value={
                @NamedQuery(name="getPatronByUserName", query="from Patron " +
                        "where username = :username")
        }

)
@Entity
@Table(name = "patron")
@Data
public class Patron {

    //COLUMN FIELDS
    @Id
    @Column(name="id", columnDefinition="serial primary key")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email_address")
    private String email;

    @Column(name = "phone_number")
    private String phone;

    @Column(name = "username")
    private String username;

    @Column(name = "password_hash")
    private String passHash;

    @Column(name = "registration_date")
    private Date registrationDate;

    @PrePersist
    void onCreate() {
        if (this.registrationDate == null) {
            this.registrationDate = new java.sql.Date(new java.util.Date().getTime());
        }
    }

    public String dateAsString() {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        return dateFormat.format(registrationDate);
    }

}

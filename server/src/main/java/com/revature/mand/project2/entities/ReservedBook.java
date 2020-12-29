package com.revature.mand.project2.entities;

import lombok.Data;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.sql.Date;

/**
 * <h3>Reservations Model</h3>
 * <p> Models all of the attributes of a book checked out by a patron.</p>
 *
 * @author Javier Gonzalez & Mason Underhill
 */
@NamedQueries(
        value={
                @NamedQuery(name="getReservedBookByPatronId", query="from ReservedBook " +
                        "where patron_id = :patronid and return_date IS NULL"),
                @NamedQuery(name="getReservedBookByIsbnAndPatronId",
                        query="from ReservedBook where patron_id = :patronid and isbn = :isbn " +
                                "and return_date IS NULL"),
                @NamedQuery(name="getReturnedBookByPatronId", query="from ReservedBook " +
                        "where patron_id = :patronid and return_date IS NOT NULL")
        }
)
@NamedNativeQueries(
        value={
                @NamedNativeQuery(name="isPatronEligible",query="SELECT * from reservation " +
                        "where patron_id = :patronid and isbn = :isbn and (return_date IS NULL " +
                        "OR (NOW()\\:\\:date - return_date\\:\\:date < 90))")
        }
)
@Entity
@Table(name = "reservation")
@Data
public class ReservedBook {

    //COLUMN FIELDS
    @Id
    @Column(name="reservation_id", columnDefinition="serial primary key")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reservationId;

    @Column(name = "patron_id")
    private int patronId;

    @ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinColumn(name="isbn",
            referencedColumnName="book_isbn",
            columnDefinition = "INT")
    private Book book;

    @Column(name = "reservation_date")
    private Date reservationDate;

    @Column(name = "reservation_term") //In days
    private int reservationTerm;

    @Column(name = "is_late")
    private boolean isLate;

    @Column(name = "due_date")
    private Date dueDate;

    @Column(name = "return_date")
    private Date returnDate;

    @PrePersist
    void onCreate() {
            this.reservationDate = new java.sql.Date(new java.util.Date().getTime());
            this.reservationTerm = 14;
            this.dueDate = addDays(reservationDate,reservationTerm);
    }

    public String dateAsString(java.sql.Date date) {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        return dateFormat.format(reservationDate);
    }

    public java.sql.Date addDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE,days);
        return new java.sql.Date(calendar.getTimeInMillis());
    }

}

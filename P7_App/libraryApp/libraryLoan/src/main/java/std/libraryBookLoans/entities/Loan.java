package std.libraryBookLoans.entities;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "library_book_loans", schema = "libraryum")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Loan implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 9036907798183495369L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    @Column(name = "return_date")
    private String returnDate;
    @NotNull
    @Column(name = "postponed")
    private Boolean postponed;
    @NotNull
    @Column(name = "returned")
    private Boolean returned;
    @NotNull
    @ManyToOne(cascade = CascadeType.PERSIST)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @JoinColumn(name = "book_fk")
    private LibraryBookLoan book;
    @NotNull
    @ManyToOne(cascade = CascadeType.PERSIST)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @JoinColumn(name = "customer_fk")
    private CustomerLoan customer;

}

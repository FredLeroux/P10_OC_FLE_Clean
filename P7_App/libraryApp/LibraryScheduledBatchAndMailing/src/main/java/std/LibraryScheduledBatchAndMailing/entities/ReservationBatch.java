package std.LibraryScheduledBatchAndMailing.entities;

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

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "library_book_reservations", schema = "libraryum")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReservationBatch implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 8249256567923017851L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "notification_date")
    private String notificationDate;
    @Column(name = "canceled_status")
    private Boolean canceledStatus;
    @Column(name = "priority")
    private Integer priority;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @JoinColumn(name = "library_book_fk")
    private LibraryBookBatch book;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @JoinColumn(name = "library_customer_fk")
    private CustomerBatch customer;

}

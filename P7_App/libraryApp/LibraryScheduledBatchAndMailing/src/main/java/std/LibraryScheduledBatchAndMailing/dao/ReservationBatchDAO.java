package std.LibraryScheduledBatchAndMailing.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import std.LibraryScheduledBatchAndMailing.entities.ReservationBatch;

public interface ReservationBatchDAO extends JpaRepository<ReservationBatch, Integer> {

    public List<ReservationBatch> findByPriorityAndCanceledStatusFalse(Integer priority);

    public List<ReservationBatch> findByPriorityAndNotificationDateNullAndCanceledStatusFalse(Integer priority);

    public Optional<ReservationBatch> findByCustomerCustomerEmailAndBookTitleAndCanceledStatusFalse(
	    String customerEmail, String bookTitle);

}

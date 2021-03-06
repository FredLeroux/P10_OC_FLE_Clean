package std.libraryReservations.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import std.libraryReservations.entities.Reservation;

@Repository
public interface LibraryReservationDAO extends JpaRepository<Reservation, Integer> {

    @Override
    public Optional<Reservation> findById(Integer id);

    public Optional<Reservation> findByBookIdAndPriorityAndCanceledStatusFalse(Integer bookId, Integer priority);

    public List<Reservation> findByBookIdAndCanceledStatusFalse(Integer bookId);

    public List<Reservation> findByBookTitleAndCanceledStatusFalse(String booktitle);

    public List<Reservation> findByCustomerCustomerEmailAndCanceledStatusFalse(String customerEmail);

}

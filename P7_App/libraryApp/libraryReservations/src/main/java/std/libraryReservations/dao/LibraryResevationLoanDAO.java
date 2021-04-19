package std.libraryReservations.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import std.libraryReservations.entities.LibraryLoanForReservation;

public interface LibraryResevationLoanDAO extends JpaRepository<LibraryLoanForReservation, Integer> {

    Optional<LibraryLoanForReservation> findByBookTitleAndCustomerIdAndReturnedFalse(String bookTiltle,
	    Integer customerId);
}

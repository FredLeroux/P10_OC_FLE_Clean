package std.libraryBookLoans.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import std.libraryBookLoans.entities.LibraryReservationForLoan;

public interface LibraryReservationForLoanDAO extends JpaRepository<LibraryReservationForLoan, Integer> {

    public Optional<LibraryReservationForLoan> findByBookIdAndCustomerIdAndCanceledStatusFalse(Integer id,
	    Integer customerID);

    public Optional<LibraryReservationForLoan> findByIdAndCanceledStatusFalse(Integer id);

    public List<LibraryReservationForLoan> findByBookIdAndCanceledStatusFalse(Integer bookId);

}

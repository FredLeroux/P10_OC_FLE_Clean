package std.libraryReservations.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import std.libraryReservations.entities.LibraryCustomerForReservation;

public interface LibraryReservationsCustomerDAO extends JpaRepository<LibraryCustomerForReservation, Integer> {

    Optional<LibraryCustomerForReservation> findByCustomerEmail(String eMail);
}

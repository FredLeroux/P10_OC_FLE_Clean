package std.libraryReservations.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import std.libraryReservations.entities.LibraryBookForReservation;

public interface LibraryReservationsBookDAO extends JpaRepository<LibraryBookForReservation, Integer> {

    public Optional<LibraryBookForReservation> findOneById(Integer id);
}

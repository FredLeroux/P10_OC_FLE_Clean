package std.libraryReservations.service;

import java.util.List;

import std.libraryReservations.dto.ReservationDTO;

public interface LibraryReservationService {

    public List<ReservationDTO> getAllBookReservations(String bookTitle);

    public void createReservation(Integer bookId, String customerEmail);

}

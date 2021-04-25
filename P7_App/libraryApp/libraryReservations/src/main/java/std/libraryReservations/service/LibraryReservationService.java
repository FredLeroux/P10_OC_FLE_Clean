package std.libraryReservations.service;

import java.util.List;

import std.libraryReservations.dto.NotificationReservationDTO;
import std.libraryReservations.dto.ReservationDTO;

public interface LibraryReservationService {

    public List<ReservationDTO> getAllBookReservations(String bookTitle);

    public void createReservation(Integer bookId, String customerEmail);

    public List<ReservationDTO> getAllCustomerReservations(String customerEmail);

    public void cancelReservation(Integer reservationId);

    public NotificationReservationDTO customerToNotified(Integer bookId, Integer priority);
}

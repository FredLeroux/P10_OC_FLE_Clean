package std.libraryReservations.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import std.libraryReservations.dto.NotificationReservationDTO;
import std.libraryReservations.dto.ReservationDTO;
import std.libraryReservations.service.LibraryReservationService;

@RestController
public class LibraryReservationsController {

    @Autowired
    public LibraryReservationService service;

    @PostMapping(value = "/createReservation")
    public void createReservation(@RequestParam(value = "bookId") Integer bookId,
	    @RequestParam(value = "customerEmail") String customerEmail) {
	service.createReservation(bookId, customerEmail);
    }

    @GetMapping(value = "/customerReservations")
    public List<ReservationDTO> customerReservations(@RequestParam(value = "customerEmail") String customerEmail) {
	return service.getAllCustomerReservations(customerEmail);
    }

    @PostMapping(value = "/cancelReservation")
    public void cancelReservation(@RequestParam(value = "reservationId") Integer reservationId) {
	service.cancelReservation(reservationId);
    }

    @GetMapping(value = "/customerToNotified")
    public NotificationReservationDTO customerToNotified(@RequestParam(value = "bookId") Integer bookId,
	    @RequestParam(value = "priority") Integer priority) {
	return service.customerToNotified(bookId, priority);
    }
}

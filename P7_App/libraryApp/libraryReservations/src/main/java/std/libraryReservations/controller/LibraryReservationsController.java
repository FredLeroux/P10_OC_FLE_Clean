package std.libraryReservations.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}

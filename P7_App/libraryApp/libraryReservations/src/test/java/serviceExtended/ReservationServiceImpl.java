package serviceExtended;

import java.util.List;

import std.libraryReservations.dto.ReservationDTO;
import std.libraryReservations.entities.LibraryBookForReservation;
import std.libraryReservations.entities.LibraryCustomerForReservation;
import std.libraryReservations.entities.Reservation;
import std.libraryReservations.service.LibraryReservationServiceImpl;

public class ReservationServiceImpl extends LibraryReservationServiceImpl {

    @Override
    public Reservation reservation(LibraryBookForReservation book, LibraryCustomerForReservation customer) {
	// TODO Auto-generated method stub
	return super.reservation(book, customer);
    }

    @Override
    public Integer priority(List<ReservationDTO> list, Integer customerId) {
	// TODO Auto-generated method stub
	return super.priority(list, customerId);
    }

    @Override
    public LibraryCustomerForReservation customer(String eMail) {
	// TODO Auto-generated method stub
	return super.customer(eMail);
    }

    @Override
    public LibraryBookForReservation book(Integer id) {
	// TODO Auto-generated method stub
	return super.book(id);
    }

    @Override
    public LibraryBookForReservation updateBookNbReservation(LibraryBookForReservation book) {
	// TODO Auto-generated method stub
	return super.updateBookNbReservation(book);
    }

    @Override
    public Reservation cancelReservationUpdate(Integer reservationId) {
	// TODO Auto-generated method stub
	return super.cancelReservationUpdate(reservationId);
    }

    @Override
    public LibraryBookForReservation cancelReservationUpdateBook(LibraryBookForReservation book) {
	// TODO Auto-generated method stub
	return super.cancelReservationUpdateBook(book);
    }

}

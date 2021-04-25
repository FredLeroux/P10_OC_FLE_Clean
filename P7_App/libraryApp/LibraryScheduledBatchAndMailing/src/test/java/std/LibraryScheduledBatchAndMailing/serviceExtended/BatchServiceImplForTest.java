package std.LibraryScheduledBatchAndMailing.serviceExtended;

import java.util.List;

import std.LibraryScheduledBatchAndMailing.dto.ReservationToNotifiedInfoDTO;
import std.LibraryScheduledBatchAndMailing.entities.LibraryBookBatch;
import std.LibraryScheduledBatchAndMailing.entities.ReservationBatch;
import std.LibraryScheduledBatchAndMailing.service.BatchServiceImpl;

public class BatchServiceImplForTest extends BatchServiceImpl {

    @Override
    public ReservationBatch setNotificationDateToNow(ReservationBatch reservation) {
	// TODO Auto-generated method stub
	return super.setNotificationDateToNow(reservation);
    }

    @Override
    public ReservationToNotifiedInfoDTO reservationToNotifiedInfoDTO(ReservationBatch reservation) {
	// TODO Auto-generated method stub
	return super.reservationToNotifiedInfoDTO(reservation);
    }

    @Override
    public List<ReservationBatch> updateReservationsCancelStatus(List<ReservationBatch> reservations,
	    Boolean booleanValue) {
	// TODO Auto-generated method stub
	return super.updateReservationsCancelStatus(reservations, booleanValue);
    }

    @Override
    public List<String> canceledReservationLinkedBooksTitles(List<ReservationBatch> canceledReservationList) {
	// TODO Auto-generated method stub
	return super.canceledReservationLinkedBooksTitles(canceledReservationList);
    }

    @Override
    public void updateReservations(List<ReservationBatch> listToSave) {
	// TODO Auto-generated method stub
	super.updateReservations(listToSave);
    }

    @Override
    public List<ReservationBatch> reservationsListToSave(List<ReservationBatch> canceledreservations,
	    List<ReservationBatch> nextPrioritary) {
	// TODO Auto-generated method stub
	return super.reservationsListToSave(canceledreservations, nextPrioritary);
    }

    @Override
    public List<LibraryBookBatch> updateBooksNumberOfreservation(List<LibraryBookBatch> booksListToUpdate,
	    Integer toAdd) {
	// TODO Auto-generated method stub
	return super.updateBooksNumberOfreservation(booksListToUpdate, toAdd);
    }

    @Override
    public List<LibraryBookBatch> booksListFromReservationsList(List<ReservationBatch> reservationsList) {
	// TODO Auto-generated method stub
	return super.booksListFromReservationsList(reservationsList);
    }

    @Override
    public void updateBooks(List<LibraryBookBatch> listOfBooksToUpdate) {
	// TODO Auto-generated method stub
	super.updateBooks(listOfBooksToUpdate);
    }
}

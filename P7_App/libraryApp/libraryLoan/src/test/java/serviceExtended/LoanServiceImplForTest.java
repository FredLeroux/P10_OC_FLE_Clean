package serviceExtended;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import std.libraryBookLoans.entities.CustomerLoan;
import std.libraryBookLoans.entities.LibraryBookLoan;
import std.libraryBookLoans.entities.LibraryBuildingLoan;
import std.libraryBookLoans.entities.LibraryReservationForLoan;
import std.libraryBookLoans.entities.LibraryRoleLoan;
import std.libraryBookLoans.entities.Loan;
import std.libraryBookLoans.service.LoanServiceImpl;

public class LoanServiceImplForTest extends LoanServiceImpl {

    @Override
    public LocalDate postponedDate(String date, Integer numberChronoOfUnit, ChronoUnit unit) {
	// TODO Auto-generated method stub
	return super.postponedDate(date, numberChronoOfUnit, unit);
    }

    @Override
    public void loanDefaultValue(Loan loan, Integer unitNumber, ChronoUnit unit) {
	// TODO Auto-generated method stub
	super.loanDefaultValue(loan, unitNumber, unit);
    }

    @Override
    public LibraryBookLoan book(Integer bookId) {
	// TODO Auto-generated method stub
	return super.book(bookId);
    }

    @Override
    public LibraryBookLoan loanedBook(Integer bookId, Integer customerId) {
	// TODO Auto-generated method stub
	return super.loanedBook(bookId, customerId);
    }

    @Override
    public LibraryBuildingLoan bookBuilding(Integer buildingId) {
	// TODO Auto-generated method stub
	return super.bookBuilding(buildingId);
    }

    @Override
    public LibraryBookLoan settedLoanedBook(Integer bookId, Integer customerId) {
	// TODO Auto-generated method stub
	return super.settedLoanedBook(bookId, customerId);
    }

    @Override
    public CustomerLoan customerLoan(Integer customerId) {
	// TODO Auto-generated method stub
	return super.customerLoan(customerId);
    }

    @Override
    public LibraryRoleLoan roleLoan(Integer id) {
	// TODO Auto-generated method stub
	return super.roleLoan(id);
    }

    @Override
    public CustomerLoan settedCustomerLoan(Integer customerId) {
	// TODO Auto-generated method stub
	return super.settedCustomerLoan(customerId);
    }

    @Override
    public Loan createLoanUsingBookId(Integer customerId, Integer bookId, Integer unitNumber, String unit) {
	// TODO Auto-generated method stub
	return super.createLoanUsingBookId(customerId, bookId, unitNumber, unit);
    }

    @Override
    public LibraryReservationForLoan reservation(Integer reservationId) {
	// TODO Auto-generated method stub
	return super.reservation(reservationId);
    }

    @Override
    public Boolean isRigthCustomer(Integer customerId, LibraryReservationForLoan reservation) {
	// TODO Auto-generated method stub
	return super.isRigthCustomer(customerId, reservation);
    }

    @Override
    public void setBookNumberOfreservation(LibraryBookLoan book) {
	// TODO Auto-generated method stub
	super.setBookNumberOfreservation(book);
    }

    @Override
    public LibraryBookLoan updateBookFromReservation(LibraryBookLoan book) {
	// TODO Auto-generated method stub
	return super.updateBookFromReservation(book);
    }

    @Override
    public Loan createLoanUsingBook(Integer customerId, LibraryBookLoan book, Integer unitNumber, String unit) {
	// TODO Auto-generated method stub
	return super.createLoanUsingBook(customerId, book, unitNumber, unit);
    }

    @Override
    public LibraryReservationForLoan updateReservationOnLoan(LibraryReservationForLoan reservation) {
	// TODO Auto-generated method stub
	return super.updateReservationOnLoan(reservation);
    }

    @Override
    public LibraryReservationForLoan updatePriority(LibraryReservationForLoan reservation) {
	// TODO Auto-generated method stub
	return super.updatePriority(reservation);
    }

    @Override
    public List<LibraryReservationForLoan> reservationUpdatedPriorityList(Integer bookId) {
	// TODO Auto-generated method stub
	return super.reservationUpdatedPriorityList(bookId);
    }

    @Override
    public void updateReservationsPriority(Integer bookId) {
	// TODO Auto-generated method stub
	super.updateReservationsPriority(bookId);
    }

}

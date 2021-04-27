package serviceExtended;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import std.libraryBookLoans.entities.CustomerLoan;
import std.libraryBookLoans.entities.LibraryBookLoan;
import std.libraryBookLoans.entities.LibraryBuildingLoan;
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

}

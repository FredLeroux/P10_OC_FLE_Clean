package std.libraryBookLoans.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import std.libraryBookLoans.controller.exception.LoanUnknown;
import std.libraryBookLoans.dao.CustomerLoanDAO;
import std.libraryBookLoans.dao.LibraryBookLoanDAO;
import std.libraryBookLoans.dao.LibraryBuildingLoanDAO;
import std.libraryBookLoans.dao.LibraryRoleLoanDAO;
import std.libraryBookLoans.dao.LoanDAO;
import std.libraryBookLoans.dto.LoanInfoDTO;
import std.libraryBookLoans.entities.CustomerLoan;
import std.libraryBookLoans.entities.LibraryBookLoan;
import std.libraryBookLoans.entities.LibraryBuildingLoan;
import std.libraryBookLoans.entities.LibraryRoleLoan;
import std.libraryBookLoans.entities.Loan;

@Service
public class LoanServiceImpl implements LoanService {

	@Autowired
	LoanDAO loanDAO;

	@Autowired
	CustomerLoanDAO cust;

	@Autowired
	LibraryRoleLoanDAO role;

	@Autowired
	LibraryBookLoanDAO book;

	@Autowired
	LibraryBuildingLoanDAO building;

	private ModelMapper mapper = new ModelMapper();
	private LoanInfoDTO loanInfoDTO = new LoanInfoDTO();

	@Override
	public List<LoanInfoDTO> customerLoans(String authUserName) {
		Integer customerId = cust.findOneByCustomerEmail(authUserName).get().getId();
		List<Loan> list = loanDAO.findByCustomerIdAndReturnedFalse(customerId);
		return (List<LoanInfoDTO>) list.stream().map(O -> mapping(O, loanInfoDTO)).collect(Collectors.toList());
	}

	@Override
	public void createLoan(Integer custommerId, Integer bookId, Integer period) {
		Loan loan = new Loan();
		loanDefaultValue(loan, period);
		loan.setBook(settedLoanedBook(bookId));
		loan.setCustomer(settedCustommerLoan(custommerId));
		loanDAO.saveAndFlush(loan);

	}

	@Override
	public void returnLoan(Integer loanId, Integer customerId) {
		Optional<Loan> optLoan = (loanDAO.findByIdAndCustomerId(loanId, customerId));
		if (optLoan.isPresent()) {
			Loan loan = optLoan.get();
			loan.setReturned(true);
			loan.getBook().setAvailability(true);
			loanDAO.saveAndFlush(loan);
		}else {
			throw new LoanUnknown("Erreur aucune correspondance");
		}
	}

	@Override
	public void postponeLoan(Integer loanId, String userName, Integer postponeDays, ArrayList<DayOfWeek> daysOffList,
			ArrayList<LocalDate> holidays) {
		Optional<Loan> optLoan = (loanDAO.findByIdAndCustomerCustomerEmail(loanId, userName));
		if (optLoan.isPresent()) {
			Loan loan = optLoan.get();
			loan.setReturnDate(
					posponeDaysOffTakedInAccount(loan.getReturnDate(), postponeDays, daysOffList, holidays).toString());
			loan.setPostponed(true);
			loanDAO.saveAndFlush(loan);
		}else {
			//TODO exception
			throw new NullPointerException();
		}

	}

	private LocalDate posponeDaysOffTakedInAccount(String date, Integer daysToAdd, ArrayList<DayOfWeek> daysOffList,
			ArrayList<LocalDate> holidays) {
		List<DayOfWeek> daysOff = daysOffList != null ? daysOffList : Collections.emptyList();
		List<LocalDate> holidayList = holidays != null ? holidays : Collections.emptyList();
		Boolean isDayOff = true;
		LocalDate postponed = postponedDate(date, daysToAdd);
		while (isDayOff) {
			if (daysOff.contains(postponed.getDayOfWeek()) || holidayList.contains(postponed)) {
				postponed = postponed.plusDays(1);
			} else {
				isDayOff = false;
			}
		}
		return postponed;
	}

	private void loanDefaultValue(Loan loan, Integer period) {
		loan.setReturnDate(postponedDate(LocalDate.now().toString(), period).toString());
		loan.setReturned(false);
		loan.setPostponed(false);
	}

	private LibraryBookLoan settedLoanedBook(Integer bookId) {
		LibraryBookLoan settedBook = loanedBook(bookId);
		settedBook.setLibraryBuilding(bookBuilding(settedBook.getLibraryBuilding().getId()));
		settedBook.setAvailability(false);
		return settedBook;
	}

	private LibraryBookLoan loanedBook(Integer bookId) {
		Optional<LibraryBookLoan> optBook = book.findById(bookId);
		if (optBook.isPresent()) {
			if (optBook.get().getAvailability()) {
				return optBook.get();
			}
			// TODO exception
			return null;

		}
		// TODO exception
		return null;
	}

	private LibraryBuildingLoan bookBuilding(Integer buildingId) {
		Optional<LibraryBuildingLoan> optBuilding = building.findById(buildingId);
		if (optBuilding.isPresent()) {
			return optBuilding.get();
		}
		// TODO exception
		return null;
	}

	private CustomerLoan settedCustommerLoan(Integer id) {
		CustomerLoan customerLoan = customerLoan(id);
		customerLoan.setRole(roleLoan(customerLoan.getRole().getId()));
		return customerLoan;
	}

	private CustomerLoan customerLoan(Integer id) {
		Optional<CustomerLoan> optCustomer = cust.findById(id);
		if (optCustomer.isPresent()) {
			return optCustomer.get();
		}
		// TODO exception
		return null;
	}

	private LibraryRoleLoan roleLoan(Integer id) {
		Optional<LibraryRoleLoan> optRole = role.findById(id);
		if (optRole.isPresent()) {
			return optRole.get();
		}
		// TODO exception
		return null;
	}

	private LocalDate postponedDate(String date, Integer daysToAdd) {
		return LocalDate.parse(date).plusDays(daysToAdd);
	}

	@SuppressWarnings("unchecked")
	private <O extends Object> O mapping(Object source, O destination) {
		return (O) mapper.map(source, destination.getClass());
	}

}

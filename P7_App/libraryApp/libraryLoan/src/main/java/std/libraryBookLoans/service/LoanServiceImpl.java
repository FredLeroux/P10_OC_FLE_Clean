package std.libraryBookLoans.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;



import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import std.libraryBookLoans.exceptions.BookNotAvailableException;
import std.libraryBookLoans.exceptions.BookNotFoundException;
import std.libraryBookLoans.exceptions.BuildingNotFoundException;
import std.libraryBookLoans.exceptions.ChronoUnitNotImplementedException;
import std.libraryBookLoans.exceptions.CustomerNotFoundException;
import std.libraryBookLoans.exceptions.LoanNotFoundException;
import std.libraryBookLoans.exceptions.LoanUnknownException;
import std.libraryBookLoans.exceptions.RoleNotFoundException;

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
	public void createLoan(Integer custommerId, Integer bookId, Integer unitNumber, ChronoUnit unit) {
		Loan loan = new Loan();
		loanDefaultValue(loan, unitNumber, unit);
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
		} else {
			throw new LoanUnknownException("Erreur aucune correspondance");
		}
	}

	@Override
	public void postponeLoan(Integer loanId, String userName, Integer unitNumber, ChronoUnit unit,
			ArrayList<DayOfWeek> daysOffList, ArrayList<LocalDate> holidays) {
		Optional<Loan> optLoan = (loanDAO.findByIdAndCustomerCustomerEmail(loanId, userName));
		if (optLoan.isPresent()) {
			Loan loan = optLoan.get();
			loan.setReturnDate(
					posponeDaysOffTakedInAccount(loan.getReturnDate(), unitNumber, unit, daysOffList, holidays)
							.toString());
			loan.setPostponed(true);
			loanDAO.saveAndFlush(loan);
		} else {
			throw new LoanNotFoundException();
		}

	}

	private LocalDate posponeDaysOffTakedInAccount(String date, Integer unitNumber, ChronoUnit unit,
			ArrayList<DayOfWeek> daysOffList, ArrayList<LocalDate> holidays) {
		List<DayOfWeek> daysOff = daysOffList != null ? daysOffList : Collections.emptyList();
		List<LocalDate> holidayList = holidays != null ? holidays : Collections.emptyList();
		Boolean isDayOff = true;
		LocalDate postponed = postponedDate(date, unitNumber, unit);
		while (isDayOff) {
			if (daysOff.contains(postponed.getDayOfWeek()) || holidayList.contains(postponed)) {
				postponed = postponed.plusDays(1);
			} else {
				isDayOff = false;
			}
		}
		return postponed;
	}

	private void loanDefaultValue(Loan loan, Integer unitNumber, ChronoUnit unit) {
		loan.setReturnDate(postponedDate(LocalDate.now().toString(), unitNumber, unit).toString());
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
			throw new BookNotAvailableException();
		}
		throw new BookNotFoundException();
	}

	private LibraryBuildingLoan bookBuilding(Integer buildingId) {
		Optional<LibraryBuildingLoan> optBuilding = building.findById(buildingId);
		if (optBuilding.isPresent()) {
			return optBuilding.get();
		}
		throw new BuildingNotFoundException();
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
		throw new CustomerNotFoundException();
	}

	private LibraryRoleLoan roleLoan(Integer id) {
		Optional<LibraryRoleLoan> optRole = role.findById(id);
		if (optRole.isPresent()) {
			return optRole.get();
		}
		throw new RoleNotFoundException();
	}

	/**
	 *
	 * @param date
	 * @param numberChronoOfUnit
	 * @param unit               is the ChronoUnit wished,waiting for
	 *                           days/weeks/months/years only
	 * @return date postponed of daysToAdd
	 *
	 */
	private LocalDate postponedDate(String date, Integer numberChronoOfUnit, ChronoUnit unit) {
		if (ChronoUnit.DAYS.equals(unit)) {
			return LocalDate.parse(date).plusDays(numberChronoOfUnit);
		} else if (ChronoUnit.WEEKS.equals(unit)) {
			return LocalDate.parse(date).plusWeeks(numberChronoOfUnit);
		} else if (ChronoUnit.MONTHS.equals(unit)) {
			return LocalDate.parse(date).plusMonths(numberChronoOfUnit);
		} else if (ChronoUnit.YEARS.equals(unit)) {
			return LocalDate.parse(date).plusYears(numberChronoOfUnit);
		}
		throw new ChronoUnitNotImplementedException();
	}

	@SuppressWarnings("unchecked")
	private <O extends Object> O mapping(Object source, O destination) {
		return (O) mapper.map(source, destination.getClass());
	}

}

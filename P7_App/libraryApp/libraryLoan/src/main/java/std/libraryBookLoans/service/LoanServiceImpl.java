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
import std.libraryBookLoans.dao.LibraryReservationForLoanDAO;
import std.libraryBookLoans.dao.LibraryRoleLoanDAO;
import std.libraryBookLoans.dao.LoanDAO;
import std.libraryBookLoans.dto.LoanInfoDTO;
import std.libraryBookLoans.dto.ReservableBookExamplaryDatedDTO;
import std.libraryBookLoans.dto.ReservableBookLinkedLoanDTO;
import std.libraryBookLoans.entities.CustomerLoan;
import std.libraryBookLoans.entities.LibraryBookLoan;
import std.libraryBookLoans.entities.LibraryBuildingLoan;
import std.libraryBookLoans.entities.LibraryReservationForLoan;
import std.libraryBookLoans.entities.LibraryRoleLoan;
import std.libraryBookLoans.entities.Loan;
import std.libraryBookLoans.exceptions.BookNotAvailableException;
import std.libraryBookLoans.exceptions.BookNotFoundException;
import std.libraryBookLoans.exceptions.BuildingNotFoundException;
import std.libraryBookLoans.exceptions.ChronoUnitNotImplementedException;
import std.libraryBookLoans.exceptions.CustomerNotFoundException;
import std.libraryBookLoans.exceptions.LoanNotFoundException;
import std.libraryBookLoans.exceptions.LoanUnknownException;
import std.libraryBookLoans.exceptions.ReservationNotFoundException;
import std.libraryBookLoans.exceptions.RoleNotFoundException;

@Service
public class LoanServiceImpl implements LoanService {

    @Autowired
    LoanDAO loanDAO;

    @Autowired
    CustomerLoanDAO customerDAO;

    @Autowired
    LibraryRoleLoanDAO roleDAO;

    @Autowired
    LibraryBookLoanDAO bookDAO;

    @Autowired
    LibraryBuildingLoanDAO buildingDAO;

    @Autowired
    LibraryReservationForLoanDAO reservationDAO;

    private ModelMapper mapper = new ModelMapper();
    private LoanInfoDTO loanInfoDTO = new LoanInfoDTO();
    private ReservableBookLinkedLoanDTO reservableBookLinkedLoan = new ReservableBookLinkedLoanDTO();

    @Override
    public List<LoanInfoDTO> customerLoans(String authUserName) {
	Integer customerId = customerDAO.findOneByCustomerEmail(authUserName).get().getId();
	List<Loan> list = loanDAO.findByCustomerIdAndReturnedFalse(customerId);
	return list.stream().map(O -> mapping(O, loanInfoDTO)).collect(Collectors.toList());
    }

    @Override
    public void createLoan(Integer customerId, Integer bookId, Integer unitNumber, ChronoUnit unit) {
	if (!isBookReserved(customerId, bookId)) {
	    createAndSaveLoan(customerId, bookId, unitNumber, unit);
	} else {
	    throw new BookNotAvailableException("Loan service : book reserved or customer alreadyloaned it");
	}
    }

    @Override
    public void createLoanFromReservation(Integer reservationId, Integer customerId, Integer unitNumber,
	    ChronoUnit unit) {
	LibraryReservationForLoan reservation = reservation(reservationId);
	if (isRigthCustomer(customerId, reservation)) {
	    createAndSaveLoan(customerId, reservation.getBook(), unitNumber, unit);
	    reservation.setCanceledStatus(true);
	    reservationDAO.saveAndFlush(reservation);
	    LibraryBookLoan book = book(reservation.getBook().getId());
	    book.setAvailability(false);
	    setBookNumberOfreservation(book);
	    bookDAO.saveAndFlush(book);
	    updateReservationsPriority(book.getId());
	} else {
	    throw new BookNotAvailableException("Loan service: customer not corresponding to the one on reservation ");
	}

    }

    private LibraryReservationForLoan reservation(Integer reservationId) {
	if (reservationDAO.findByIdAndCanceledStatusFalse(reservationId).isPresent()) {
	    return reservationDAO.findById(reservationId).get();
	} else {
	    throw new ReservationNotFoundException("Reservation ref = [" + reservationId + "] not found.");
	}
    }

    private Boolean isRigthCustomer(Integer customerId, LibraryReservationForLoan reservation) {
	return customerId == reservation.getCustomer().getId();
    }

    private void setBookNumberOfreservation(LibraryBookLoan book) {
	if (book.getNumberOfReservations() != 0) {
	    book.setNumberOfReservations(book.getNumberOfReservations() - 1);
	}
    }

    private void updateReservationsPriority(Integer bookId) {
	if (!reservationDAO.findByBookIdAndCanceledStatusFalse(bookId).isEmpty()) {
	    reservationDAO.saveAll(reservationDAO.findByBookIdAndCanceledStatusFalse(bookId).stream()
		    .map(o -> updatePriority(o)).collect(Collectors.toList()));
	}
    }

    private LibraryReservationForLoan updatePriority(LibraryReservationForLoan reservation) {
	reservation.setPriority(reservation.getPriority() - 1);
	return reservation;
    }

    private void createAndSaveLoan(Integer customerId, Integer bookId, Integer unitNumber, ChronoUnit unit) {
	Loan loan = new Loan();
	loanDefaultValue(loan, unitNumber, unit);
	loan.setBook(settedLoanedBook(bookId, customerId));
	loan.setCustomer(settedCustommerLoan(customerId));
	loanDAO.saveAndFlush(loan);
    }

    private void createAndSaveLoan(Integer customerId, LibraryBookLoan book, Integer unitNumber, ChronoUnit unit) {
	Loan loan = new Loan();
	loanDefaultValue(loan, unitNumber, unit);
	loan.setBook(book);
	loan.setCustomer(settedCustommerLoan(customerId));
	loanDAO.saveAndFlush(loan);
    }

    @Override
    public void returnLoan(Integer bookId, Integer customerId) {
	Optional<Loan> optLoan = (loanDAO.findByBookIdAndCustomerIdAndReturnedFalse(bookId, customerId));
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

    private LibraryBookLoan settedLoanedBook(Integer bookId, Integer customerId) {
	LibraryBookLoan settedBook = loanedBook(bookId, customerId);
	settedBook.setLibraryBuilding(bookBuilding(settedBook.getLibraryBuilding().getId()));
	settedBook.setAvailability(false);
	return settedBook;
    }

    private LibraryBookLoan loanedBook(Integer bookId, Integer customerId) {
	LibraryBookLoan book = book(bookId);
	if (book.getAvailability() && !isTitleAlreadyLoaned(customerId, book.getTitle())) {
	    return book;
	} else {
	    throw new BookNotAvailableException("Book not available");
	}

    }

    private LibraryBookLoan book(Integer bookId) {
	if (bookDAO.findById(bookId).isPresent()) {
	    return bookDAO.findById(bookId).get();
	} else {
	    throw new BookNotFoundException("pas de livre sous" + bookId);
	}
    }

    private LibraryBuildingLoan bookBuilding(Integer buildingId) {
	Optional<LibraryBuildingLoan> optBuilding = buildingDAO.findById(buildingId);
	if (optBuilding.isPresent()) {
	    return optBuilding.get();
	}
	throw new BuildingNotFoundException();
    }

    private CustomerLoan settedCustommerLoan(Integer customerId) {
	CustomerLoan customerLoan = customerLoan(customerId);
	customerLoan.setRole(roleLoan(customerLoan.getRole().getId()));
	return customerLoan;
    }

    private CustomerLoan customerLoan(Integer customerId) {
	Optional<CustomerLoan> optCustomer = customerDAO.findById(customerId);
	if (optCustomer.isPresent()) {
	    return optCustomer.get();
	}
	throw new CustomerNotFoundException();
    }

    private LibraryRoleLoan roleLoan(Integer id) {
	Optional<LibraryRoleLoan> optRole = roleDAO.findById(id);
	if (optRole.isPresent()) {
	    return optRole.get();
	}
	throw new RoleNotFoundException();
    }

    @SuppressWarnings("unchecked")
    private <O extends Object> O mapping(Object source, O destination) {
	return (O) mapper.map(source, destination.getClass());
    }

    @Override
    public List<ReservableBookExamplaryDatedDTO> reservableBookExamplaryDTOs(List<Integer> bookIdList,
	    Integer numberChronoOfUnit, ChronoUnit unit) {
	return getReservableBookLinkedLoans(bookIdList).stream()
		.map(o -> mapReservableBookLinkedLoanDTOToExamplary(o, numberChronoOfUnit, unit))
		.collect(Collectors.toList());
    }

    private ReservableBookExamplaryDatedDTO mapReservableBookLinkedLoanDTOToExamplary(ReservableBookLinkedLoanDTO dto,
	    Integer numberChronoOfUnit, ChronoUnit unit) {
	ReservableBookExamplaryDatedDTO examplary = new ReservableBookExamplaryDatedDTO();
	examplary.setReference(dto.getBook().getId());
	examplary.setClosestReturnDate(dto.getReturnDate());
	if (dto.getPostponed()) {
	    examplary.setFarrestReturnDate("none");
	} else {
	    examplary.setFarrestReturnDate(postponedDate(dto.getReturnDate(), numberChronoOfUnit, unit).toString());
	}

	return examplary;

    }

    @Override
    public List<ReservableBookLinkedLoanDTO> getReservableBookLinkedLoans(List<Integer> bookIdList) {
	return loanDAO.findByReturnedFalseAndBookIdIn(bookIdList).stream()
		.map(o -> convertLoanToReservableBookLinkedLoan(o)).collect(Collectors.toList());
    }

    private ReservableBookLinkedLoanDTO convertLoanToReservableBookLinkedLoan(Loan loan) {
	return mapper.map(loan, reservableBookLinkedLoan.getClass());
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

    private Boolean isBookReserved(Integer customerId, Integer bookId) {
	return reservationDAO.findByBookIdAndCustomerIdAndCanceledStatusFalse(bookId, customerId).isPresent();
    }

    private Boolean isTitleAlreadyLoaned(Integer customerId, String bookTitle) {
	return loanDAO.findByBookTitleAndCustomerIdAndReturnedFalse(bookTitle, customerId).isPresent();

    }

}

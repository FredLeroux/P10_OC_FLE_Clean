package std.libraryBookLoans.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang.ObjectUtils;
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
    public void createLoan(Integer customerId, Integer bookId, Integer unitNumber, String unit) {
	if (!isBookReserved(customerId, bookId)) {
	    saveLoan(createLoanUsingBookId(customerId, bookId, unitNumber, unit));
	} else {
	    throw new BookNotAvailableException("Loan service : book reserved or customer alreadyloaned it");
	}
    }

    protected Loan createLoanUsingBookId(Integer customerId, Integer bookId, Integer unitNumber, String unit) {
	Loan loan = new Loan();
	loanDefaultValue(loan, unitNumber, ChronoUnit.valueOf(unit.toUpperCase()));
	loan.setBook(settedLoanedBook(bookId, customerId));
	loan.setCustomer(settedCustomerLoan(customerId));
	return loan;
    }

    private Boolean isBookReserved(Integer customerId, Integer bookId) {
	return reservationDAO.findByBookIdAndCustomerIdAndCanceledStatusFalse(bookId, customerId).isPresent();
    }

    @Override
    public void createLoanFromReservation(Integer reservationId, Integer customerId, Integer unitNumber, String unit) {
	LibraryReservationForLoan reservation = reservation(reservationId);
	if (isRigthCustomer(customerId, reservation)) {
	    LibraryBookLoan book = reservation.getBook();
	    saveLoan(createLoanUsingBook(customerId, book, unitNumber, unit));
	    reservationDAO.saveAndFlush(updateReservationOnLoan(reservation));
	    updateReservationsPriority(book.getId());
	} else {
	    throw new BookNotAvailableException("Loan service: customer not corresponding to the one on reservation ");
	}

    }

    protected void setBookNumberOfreservation(LibraryBookLoan book) {
	Integer nbOfReservation = (Integer) ObjectUtils.defaultIfNull(book.getNumberOfReservations(), 0);
	if (nbOfReservation != 0) {
	    book.setNumberOfReservations(nbOfReservation - 1);
	}
    }

    protected Loan createLoanUsingBook(Integer customerId, LibraryBookLoan book, Integer unitNumber, String unit) {
	Loan loan = new Loan();
	loanDefaultValue(loan, unitNumber, ChronoUnit.valueOf(unit.toUpperCase()));
	loan.setBook(updateBookFromReservation(book));
	loan.setCustomer(settedCustomerLoan(customerId));
	return loan;
    }

    protected LibraryBookLoan updateBookFromReservation(LibraryBookLoan book) {
	book.setAvailability(false);
	setBookNumberOfreservation(book);
	return book;
    }

    protected LibraryReservationForLoan updateReservationOnLoan(LibraryReservationForLoan reservation) {
	reservation.setCanceledStatus(true);
	reservation.setPriority(-1);
	return reservation;
    }

    /**
     *
     * @param loan       the loan to update
     * @param unitNumber the number of unit to add to postpone date
     * @param unit       the unit to postpone date
     * @apiNote default value set to 4 weeks of postpone
     */
    protected void loanDefaultValue(Loan loan, Integer unitNumber, ChronoUnit unit) {
	unitNumber = (Integer) ObjectUtils.defaultIfNull(unitNumber, 4);
	unit = (ChronoUnit) ObjectUtils.defaultIfNull(unit, ChronoUnit.WEEKS);
	loan.setReturnDate(postponedDate(LocalDate.now().toString(), unitNumber, unit).toString());
	loan.setReturned(false);
	loan.setPostponed(false);
    }

    protected LibraryReservationForLoan reservation(Integer reservationId) {
	if (reservationDAO.findByIdAndCanceledStatusFalse(reservationId).isPresent()) {
	    return reservationDAO.findByIdAndCanceledStatusFalse(reservationId).get();
	} else {
	    throw new ReservationNotFoundException("Reservation ref = [" + reservationId + "] not found.");
	}
    }

    protected Boolean isRigthCustomer(Integer customerId, LibraryReservationForLoan reservation) {
	return customerId.equals(reservation.getCustomer().getId());
    }

    protected List<LibraryReservationForLoan> reservationUpdatedPriorityList(Integer bookId) {
	return reservationDAO.findByBookIdAndCanceledStatusFalse(bookId).stream().map(o -> updatePriority(o))
		.collect(Collectors.toList());
    }

    protected void updateReservationsPriority(Integer bookId) {
	if (!reservationDAO.findByBookIdAndCanceledStatusFalse(bookId).isEmpty()) {
	    reservationDAO.saveAll(reservationUpdatedPriorityList(bookId));
	}
    }

    protected LibraryReservationForLoan updatePriority(LibraryReservationForLoan reservation) {
	reservation.setPriority(reservation.getPriority() - 1);
	if (reservation.getPriority() < 0) {
	    reservation.setPriority(0);
	}
	return reservation;
    }

    private void saveLoan(Loan loan) {
	loanDAO.saveAndFlush(loan);
    }

    @Override
    public void returnLoan(Integer bookId, Integer customerId) {
	Optional<Loan> optLoan = loanDAO.findByBookIdAndCustomerIdAndReturnedFalse(bookId, customerId);
	if (optLoan.isPresent()) {
	    loanDAO.saveAndFlush(updatedLoan(optLoan.get()));
	} else {
	    throw new LoanUnknownException("Erreur aucune correspondance");
	}
    }

    protected Loan updatedLoan(Loan loan) {
	loan.setReturned(true);
	loan.getBook().setAvailability(true);
	return loan;
    }

    @Override
    public void postponeLoan(Integer loanId, String userName, Integer unitNumber, String unit,
	    ArrayList<DayOfWeek> daysOffList, ArrayList<LocalDate> holidays) {
	Optional<Loan> optLoan = (loanDAO.findByIdAndCustomerCustomerEmail(loanId, userName));
	if (optLoan.isPresent()) {
	    Loan loan = optLoan.get();
	    loan.setReturnDate(posponeDaysOffTakedInAccount(loan.getReturnDate(), unitNumber,
		    ChronoUnit.valueOf(unit.toUpperCase()), daysOffList, holidays).toString());
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

    protected LibraryBookLoan settedLoanedBook(Integer bookId, Integer customerId) {
	LibraryBookLoan settedBook = loanedBook(bookId, customerId);
	settedBook.setLibraryBuilding(bookBuilding(settedBook.getLibraryBuilding().getId()));
	settedBook.setAvailability(false);
	return settedBook;
    }

    protected LibraryBookLoan loanedBook(Integer bookId, Integer customerId) {
	LibraryBookLoan book = book(bookId);
	if (book.getAvailability() && !isTitleAlreadyLoaned(customerId, book.getTitle())) {
	    return book;
	} else {
	    throw new BookNotAvailableException("Book not available");
	}

    }

    protected LibraryBookLoan book(Integer bookId) {
	if (bookDAO.findById(bookId).isPresent()) {
	    return bookDAO.findById(bookId).get();
	} else {
	    throw new BookNotFoundException("pas de livre sous " + bookId);
	}
    }

    protected LibraryBuildingLoan bookBuilding(Integer buildingId) {
	Optional<LibraryBuildingLoan> optBuilding = buildingDAO.findById(buildingId);
	if (optBuilding.isPresent()) {
	    return optBuilding.get();
	}
	throw new BuildingNotFoundException();
    }

    protected CustomerLoan settedCustomerLoan(Integer customerId) {
	CustomerLoan customerLoan = customerLoan(customerId);
	customerLoan.setRole(roleLoan(customerLoan.getRole().getId()));
	return customerLoan;
    }

    protected CustomerLoan customerLoan(Integer customerId) {
	Optional<CustomerLoan> optCustomer = customerDAO.findById(customerId);
	if (optCustomer.isPresent()) {
	    return optCustomer.get();
	}
	throw new CustomerNotFoundException();
    }

    protected LibraryRoleLoan roleLoan(Integer id) {
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
	    Integer numberChronoOfUnit, String unit) {
	return getReservableBookLinkedLoans(bookIdList).stream().map(o -> mapReservableBookLinkedLoanDTOToExamplary(o,
		numberChronoOfUnit, ChronoUnit.valueOf(unit.toUpperCase()))).collect(Collectors.toList());
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
    protected LocalDate postponedDate(String date, Integer numberChronoOfUnit, ChronoUnit unit) {
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

    private Boolean isTitleAlreadyLoaned(Integer customerId, String bookTitle) {
	return loanDAO.findByBookTitleAndCustomerIdAndReturnedFalse(bookTitle, customerId).isPresent();

    }

}

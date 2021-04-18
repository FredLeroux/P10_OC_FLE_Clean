package std.LibraryScheduledBatchAndMailing.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import std.LibraryScheduledBatchAndMailing.dao.CustomerBatchDAO;
import std.LibraryScheduledBatchAndMailing.dao.LibraryBookBatchDAO;
import std.LibraryScheduledBatchAndMailing.dao.LoanBatchDAO;
import std.LibraryScheduledBatchAndMailing.dao.ReservationBatchDAO;
import std.LibraryScheduledBatchAndMailing.dto.CustomerBatchEmailDTO;
import std.LibraryScheduledBatchAndMailing.dto.LibraryBookBatchTitleDTO;
import std.LibraryScheduledBatchAndMailing.dto.LoanBatchMailInfoDTO;
import std.LibraryScheduledBatchAndMailing.dto.ReservationToNotifiedInfoDTO;
import std.LibraryScheduledBatchAndMailing.entities.CustomerBatch;
import std.LibraryScheduledBatchAndMailing.entities.LibraryBookBatch;
import std.LibraryScheduledBatchAndMailing.entities.LoanBatch;
import std.LibraryScheduledBatchAndMailing.entities.ReservationBatch;
import std.LibraryScheduledBatchAndMailing.exceptions.BookNotFoundException;
import std.LibraryScheduledBatchAndMailing.exceptions.CustomerNotFoundException;

@Service
public class BatchServiceImpl implements BatchService {

    @Autowired
    LoanBatchDAO loanBatchDao;

    @Autowired
    CustomerBatchDAO customerBatchDao;

    @Autowired
    LibraryBookBatchDAO libraryBookBatchDao;

    // -------- Ticket-1 issue#5 --------//
    @Autowired
    ReservationBatchDAO reservationBatchDAO;

    private ModelMapper mapper = new ModelMapper();
    private LoanBatchMailInfoDTO loanBatchMailInfoDTO = new LoanBatchMailInfoDTO();
    private CustomerBatchEmailDTO customerBatchEmailDTO = new CustomerBatchEmailDTO();
    private LibraryBookBatchTitleDTO libraryBookBatchTitleDTO = new LibraryBookBatchTitleDTO();

    @Override
    public List<LoanBatchMailInfoDTO> sortLateLoansList() {
	return unReturnedLoanDTO().stream().filter(o -> parseReturnDateToLocalDate(o).compareTo(LocalDate.now()) < 0)
		.collect(Collectors.toList());
    }

    private LocalDate parseReturnDateToLocalDate(LoanBatchMailInfoDTO dto) {
	return LocalDate.parse(dto.getReturnDate());
    }

    private List<LoanBatchMailInfoDTO> unReturnedLoanDTO() {
	return unReturnedLoan().stream().map(o -> setLoanBatchMailInfoDTO(o)).collect(Collectors.toList());
    }

    private LoanBatchMailInfoDTO setLoanBatchMailInfoDTO(LoanBatch loanBatch) {
	LoanBatchMailInfoDTO dto = mapLoanBatchMailInfoDTO(loanBatch);
	dto.setCustomer(mapCustomerBatch(customerBatch(loanBatch.getCustomer().getId())));
	dto.setBook(mapLibraryBookBatchTitleDTO(libraryBookBatch(loanBatch.getBook().getId())));
	return dto;
    }

    private LoanBatchMailInfoDTO mapLoanBatchMailInfoDTO(LoanBatch loanBatch) {
	return mappingTo(loanBatch, loanBatchMailInfoDTO);
    }

    private LibraryBookBatch libraryBookBatch(Integer id) {
	Optional<LibraryBookBatch> optLibraryBookBatch = libraryBookBatchDao.findById(id);
	if (optLibraryBookBatch.isPresent()) {
	    return optLibraryBookBatch.get();
	}
	throw new BookNotFoundException();

    }

    private LibraryBookBatchTitleDTO mapLibraryBookBatchTitleDTO(LibraryBookBatch libraryBookBatch) {
	return mappingTo(libraryBookBatch, libraryBookBatchTitleDTO);
    }

    private CustomerBatch customerBatch(Integer id) {
	Optional<CustomerBatch> optCustomerBatch = customerBatchDao.findById(id);
	if (optCustomerBatch.isPresent()) {
	    return optCustomerBatch.get();
	}
	throw new CustomerNotFoundException();

    }

    private CustomerBatchEmailDTO mapCustomerBatch(CustomerBatch customerBatch) {
	return mappingTo(customerBatch, customerBatchEmailDTO);
    }

    private List<LoanBatch> unReturnedLoan() {
	return loanBatchDao.findByReturnedFalse();
    }

    @SuppressWarnings("unchecked")
    private <O extends Object> O mappingTo(Object source, O destination) {
	return (O) mapper.map(source, destination.getClass());
    }

    // -------- Ticket-1 issue#5 --------//

    @Override
    public void updateNotificationDate(String customerEmail, String bookTitle) {
	Optional<ReservationBatch> optReservation = reservationBatchDAO
		.findByCustomerCustomerEmailAndBookTitleAndCanceledStatusFalse(customerEmail, bookTitle);
	if (optReservation.isPresent()) {
	    ReservationBatch reservation = optReservation.get();
	    reservation.setNotificationDate(LocalDate.now().toString());
	    reservationBatchDAO.saveAndFlush(reservation);
	} else {
	    throw new NullPointerException("update notification date fail null reservation");
	}
    }

    ////////////////////////////////////////////////////////

    @Override
    public List<ReservationToNotifiedInfoDTO> reservationsToCancelInfo(List<ReservationBatch> reservations) {
	if (!reservations.isEmpty()) {
	    return reservations.stream().map(o -> reservationToNotifiedInfoDTO(o)).collect(Collectors.toList());
	}
	return null;
    }

    @Override
    public void updateAndSaveReservationAndLinkedBookOnExceedDelay(List<ReservationBatch> canceledReservations,
	    List<ReservationBatch> nextPriorityReservations, Integer priority, Integer toAdd) {
	updateBooks(updateBooksNumberOfreservation(booksListFromReservationsList(canceledReservations), toAdd));
	updateReservations(updateReservationsCancelStatus(canceledReservations, true), nextPriorityReservations);

    }

    @Override
    public List<ReservationBatch> reservationsToCancelListDelayExceeded(List<ReservationBatch> reservations,
	    Long delayInDays) {
	List<ReservationBatch> list = new ArrayList<>();
	if (!reservations.isEmpty()) {
	    list = reservations.stream()
		    .filter(o -> isDateIsBeforeRefDate(parseStringToLocalDate(o.getNotificationDate()),
			    LocalDate.now().minusDays(delayInDays)))
		    .collect(Collectors.toList());
	}
	return list;
    }

    @Override
    public List<ReservationBatch> reservationsToCheckCancelation(List<ReservationBatch> reservations) {
	if (!reservations.isEmpty()) {
	    return reservations.stream().filter(o -> o.getNotificationDate() != null).collect(Collectors.toList());
	} else {
	    return new ArrayList<>();
	}

    }

    private void updateReservations(List<ReservationBatch> canceledreservations,
	    List<ReservationBatch> nextPrioritary) {
	List<ReservationBatch> listToSave = new ArrayList<ReservationBatch>();

	if (!canceledreservations.isEmpty()) {
	    canceledreservations.forEach(o -> listToSave.add(o));
	}
	if (!nextPrioritary.isEmpty()) {
	    nextPrioritary.forEach(o -> listToSave.add(o));
	}
	if (!listToSave.isEmpty()) {
	    saveReservationList(listToSave);
	}

    }

    private void saveReservationList(List<ReservationBatch> reservations) {
	if (!reservations.isEmpty()) {
	    reservationBatchDAO.saveAll(reservations);
	}
    }

    /**
     *
     * @param reservations
     * @param booleanValue
     * @return
     */
    private List<ReservationBatch> updateReservationsCancelStatus(List<ReservationBatch> reservations,
	    Boolean booleanValue) {
	if (!reservations.isEmpty()) {
	    reservations.forEach(o -> o.setCanceledStatus(booleanValue));
	    return reservations;
	} else {
	    return new ArrayList<ReservationBatch>();
	}
    }

    @Override
    public List<ReservationBatch> updatedNextPriorityReservation(List<ReservationBatch> originList, Integer priority) {
	List<ReservationBatch> nextPrioritary = originList.stream().filter(o -> o.getPriority() == (priority + 1))
		.collect(Collectors.toList());
	nextPrioritary.forEach(o -> o.setPriority(priority));
	return nextPrioritary;
    }

    private void updateBooks(List<LibraryBookBatch> listOfBooksToUpdate) {
	if (!listOfBooksToUpdate.isEmpty()) {
	    saveBooksList(listOfBooksToUpdate);
	}

    }

    /**
     *
     * @param booksListToUpdate
     * @param toAdd             value to add to the number of reservation use
     *                          negative value to minus , positive to increase the
     *                          value
     *                          {@link #setBookNumberOfReservation(LibraryBookBatch, Integer)}
     * @return list of books with numberOfReservation set with toAdd
     *
     */
    private List<LibraryBookBatch> updateBooksNumberOfreservation(List<LibraryBookBatch> booksListToUpdate,
	    Integer toAdd) {
	List<LibraryBookBatch> list = new ArrayList<>();
	if (!booksListToUpdate.isEmpty()) {
	    list.forEach(o -> setBookNumberOfReservation(o, toAdd));
	}
	return list;
    }

    /**
     *
     * @param book  the book where to change number of reservation Security which
     *              set to 0 id a number of reservation minus-1 gives a negative
     *              number
     * @param toAdd value to add to the number of reservation use negative value to
     *              minus , positive to increase the value
     */
    private void setBookNumberOfReservation(LibraryBookBatch book, Integer toAdd) {
	book.setNumberOfReservations(book.getNumberOfReservations() + toAdd);
	if (book.getNumberOfReservations() < 0) {
	    book.setNumberOfReservations(0);
	}

    }

    /**
     *
     * @param reservationsList a reservation list where to extract a books list
     * @return a list a books from a list of reservation
     */
    private List<LibraryBookBatch> booksListFromReservationsList(List<ReservationBatch> reservationsList) {
	List<LibraryBookBatch> list = new ArrayList<>();
	if (!reservationsList.isEmpty()) {
	    reservationsList.forEach(o -> list.add(o.getBook()));

	}
	return list;

    }

    private void saveBooksList(List<LibraryBookBatch> books) {
	libraryBookBatchDao.saveAll(books);
    }

    /**
     *
     * @return true if date is before referenceDate else False
     */
    private Boolean isDateIsBeforeRefDate(LocalDate date, LocalDate refDate) {
	return date.isBefore(refDate);
    }

    private LocalDate parseStringToLocalDate(String dateToParse) {
	return LocalDate.parse(dateToParse);
    }

    @Override
    public List<ReservationBatch> toNotifieds(Integer priority) {
	return reservationBatchDAO.findByPriorityAndCanceledStatusFalse(priority);
    }

    @Override
    public List<ReservationToNotifiedInfoDTO> reservationsToNotifiedBookAvailable(List<ReservationBatch> reservations) {
	if (!reservations.isEmpty()) {
	    return reservations.stream().map(o -> reservationToNotifiedInfoDTO(o)).collect(Collectors.toList());
	}
	return new ArrayList<ReservationToNotifiedInfoDTO>();
    }

    @Override
    public List<ReservationBatch> updatedNotificationDateReservations(Integer priority) {
	List<ReservationBatch> list = reservationBatchDAO
		.findByPriorityAndNotificationDateNullAndCanceledStatusFalse(priority);
	if (!list.isEmpty()) {
	    list.forEach(o -> o.setNotificationDate(LocalDate.now().toString()));
	    return list;
	} else {
	    return new ArrayList<ReservationBatch>();
	}
    }

    private ReservationToNotifiedInfoDTO reservationToNotifiedInfoDTO(ReservationBatch reservation) {
	return new ReservationToNotifiedInfoDTO(reservation.getId(), reservation.getCustomer().getCustomerEmail(),
		reservation.getBook().getTitle(), reservation.getBook().getLibraryBuilding().getName());
    }

    @Override
    public void saveReservationBatch(List<ReservationBatch> listToSave) {
	reservationBatchDAO.saveAll(listToSave);

    }

}

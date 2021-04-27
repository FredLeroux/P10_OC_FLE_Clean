package std.libraryReservations.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import std.libraryReservations.dao.LibraryReservationDAO;
import std.libraryReservations.dao.LibraryReservationsBookDAO;
import std.libraryReservations.dao.LibraryReservationsCustomerDAO;
import std.libraryReservations.dao.LibraryResevationLoanDAO;
import std.libraryReservations.dto.NotificationReservationDTO;
import std.libraryReservations.dto.ReservationDTO;
import std.libraryReservations.entities.LibraryBookForReservation;
import std.libraryReservations.entities.LibraryCustomerForReservation;
import std.libraryReservations.entities.LibraryLoanForReservation;
import std.libraryReservations.entities.Reservation;
import std.libraryReservations.exceptions.AlreadyReservedOrLoanedByCustomerException;
import std.libraryReservations.exceptions.NotFoundInDataBaseException;
import std.libraryReservations.exceptions.ReservationsListException;

@Service
public class LibraryReservationServiceImpl implements LibraryReservationService {

    @Autowired
    private LibraryReservationDAO libraryReservationDAO;

    @Autowired
    private LibraryReservationsCustomerDAO libraryReservationsCustomerDAO;

    @Autowired
    private LibraryResevationLoanDAO libraryReservationLoanDAO;

    @Autowired
    private LibraryReservationsBookDAO libraryReservationsBookDAO;

    private ModelMapper modelMapper = new ModelMapper();
    private ReservationDTO reservationDTO = new ReservationDTO();

    @Override
    public List<ReservationDTO> getAllBookReservations(String bookTitle) {
	List<ReservationDTO> list = new ArrayList<>();
	try {
	    list = libraryReservationDAO.findByBookTitleAndCanceledStatusFalse(bookTitle).stream()
		    .map(o -> reservationToReservationDTO(o)).collect(Collectors.toList());
	} catch (Exception e) {
	    throw new ReservationsListException();
	}
	return list;
    }

    @Override
    public void createReservation(Integer bookId, String customerEmail) {
	LibraryCustomerForReservation customer = customer(customerEmail);
	LibraryBookForReservation book = book(bookId);
	if (checkIfBookAlreadyLoaned(book.getTitle(), customer.getId())) {
	    throw new AlreadyReservedOrLoanedByCustomerException();
	}
	libraryReservationDAO.saveAndFlush(reservation(book, customer));
	libraryReservationsBookDAO.saveAndFlush(updateBookNbReservation(book));

    }

    protected Reservation reservation(LibraryBookForReservation book, LibraryCustomerForReservation customer) {
	Reservation reservation = new Reservation();
	reservation.setPriority(priority(getAllBookReservations(book.getTitle()), customer.getId()));
	reservation.setBook(book);
	reservation.setCustomer(customer);
	reservation.setCanceledStatus(false);
	return reservation;
    }

    protected Integer priority(List<ReservationDTO> list, Integer customerId) {
	if (list.isEmpty()) {
	    return 1;
	} else {
	    if (!checkCustomerReservation(list, customerId)) {
		throw new AlreadyReservedOrLoanedByCustomerException();
	    }
	    return list.size() + 1;
	}

    }

    protected LibraryBookForReservation updateBookNbReservation(LibraryBookForReservation book) {
	book.setNumberOfReservations(book.getNumberOfReservations() + 1);
	return book;
    }

    protected LibraryCustomerForReservation customer(String eMail) {
	if (libraryReservationsCustomerDAO.findByCustomerEmail(eMail).isPresent()) {
	    return libraryReservationsCustomerDAO.findByCustomerEmail(eMail).get();
	} else {
	    throw new NotFoundInDataBaseException("Reservation service: customer not found");
	}
    }

    protected LibraryBookForReservation book(Integer id) {
	if (libraryReservationsBookDAO.findOneById(id).isPresent()) {
	    return libraryReservationsBookDAO.findOneById(id).get();
	} else {
	    throw new NotFoundInDataBaseException("Reservation service: book not found");
	}
    }

    /*
     * Note important on comparison primitive use == Object use .equals
     */
    private Boolean checkCustomerReservation(List<ReservationDTO> list, Integer customerId) {
	return list.stream().filter(o -> o.getCustomer().getId().equals(customerId)).collect(Collectors.toList())
		.isEmpty();
    }

    private Boolean checkIfBookAlreadyLoaned(String bookTitle, Integer customerId) {
	return libraryReservationLoanDAO.findByBookTitleAndCustomerIdAndReturnedFalse(bookTitle, customerId)
		.isPresent();
    }

    @Override
    public List<ReservationDTO> getAllCustomerReservations(String customerEmail) {
	List<Reservation> customerReservations = libraryReservationDAO
		.findByCustomerCustomerEmailAndCanceledStatusFalse(customerEmail);
	List<LibraryLoanForReservation> reservationsLinkedLoan = reservationsLinkedLoan(customerReservations);
	return customerReservations.stream().map(o -> mapReservationToDTO(o, reservationsLinkedLoan))
		.collect(Collectors.toList());
    }

    protected ReservationDTO mapReservationToDTO(Reservation ent,
	    List<LibraryLoanForReservation> reservationsLinkedLoan) {
	ReservationDTO dto = new ReservationDTO();
	dto = reservationToReservationDTO(ent);
	dto.setBuildingName(ent.getBook().getLibraryBuilding().getName());
	LibraryLoanForReservation loan = linkedLoan(ent.getBook().getId(), reservationsLinkedLoan);
	dto.setReturnDate(loan.getReturnDate());
	dto.setPostpone(loan.getPostponed());
	return dto;
    }

    protected LibraryLoanForReservation linkedLoan(Integer bookId,
	    List<LibraryLoanForReservation> reservationsLinkedLoan) {
	Optional<LibraryLoanForReservation> optLoan = reservationsLinkedLoan.stream()
		.filter(o -> o.getBook().getId().equals(bookId)).findFirst();
	if (optLoan.isPresent()) {
	    return optLoan.get();
	} else {
	    throw new NullPointerException("Reservation service: reservation Linked loan return null");
	}
    }

    protected List<LibraryLoanForReservation> reservationsLinkedLoan(List<Reservation> reservations) {
	return libraryReservationLoanDAO.findByBookIdInAndAndReturnedFalse(reservationBooksId(reservations));
    }

    protected List<Integer> reservationBooksId(List<Reservation> reservations) {
	List<Integer> list = new ArrayList<>();
	if (!reservations.isEmpty()) {
	    reservations.forEach(o -> list.add(o.getBook().getId()));
	}
	return list;
    }

    @Override
    public void cancelReservation(Integer reservationId) {
	Reservation reservation = cancelReservationUpdate(reservationId);
	LibraryBookForReservation book = cancelReservationUpdateBook(reservation.getBook());
	libraryReservationDAO.saveAndFlush(reservation);
	libraryReservationsBookDAO.saveAndFlush(book);
	List<Reservation> listToUpdate = updateNextPriority(reservation);
	if (!listToUpdate.isEmpty()) {
	    libraryReservationDAO.saveAll(listToUpdate);
	}

    }

    protected Reservation cancelReservationUpdate(Integer reservationId) {
	if (libraryReservationDAO.findById(reservationId).isPresent()) {
	    Reservation reservation = libraryReservationDAO.findById(reservationId).get();
	    reservation.setCanceledStatus(true);
	    return reservation;
	} else {
	    throw new NotFoundInDataBaseException("Reservation service : reservation to cancel not found");
	}
    }

    protected LibraryBookForReservation cancelReservationUpdateBook(LibraryBookForReservation book) {
	book.setNumberOfReservations(book.getNumberOfReservations() - 1);
	if (book.getNumberOfReservations() < 0) {
	    book.setNumberOfReservations(0);
	}
	return book;
    }

    protected List<Reservation> updateNextPriority(Reservation reservation) {
	List<Reservation> list = libraryReservationDAO
		.findByBookIdAndCanceledStatusFalse(reservation.getBook().getId());
	if (!list.isEmpty()) {
	    list.forEach(o -> o.setPriority(o.getPriority() - 1));
	    return list;
	} else {
	    return new ArrayList<Reservation>();
	}

    }

    private ReservationDTO reservationToReservationDTO(Reservation reservation) {
	return (ReservationDTO) mapper(reservation, reservationDTO);
    }

    private Object mapper(Object source, Object destination) {
	return modelMapper.map(source, destination.getClass());
    }

    @Override
    public NotificationReservationDTO customerToNotified(Integer bookId, Integer priority) {
	Optional<Reservation> reservation = libraryReservationDAO.findByBookIdAndPriorityAndCanceledStatusFalse(bookId,
		priority);
	if (reservation.isPresent()) {
	    return new NotificationReservationDTO(reservation.get().getCustomer().getCustomerEmail(),
		    reservation.get().getBook().getTitle(), reservation.get().getBook().getLibraryBuilding().getName(),
		    reservation.get().getId());
	}
	return null;
    }

}

package std.libraryReservations.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sun.jersey.api.NotFoundException;

import std.libraryReservations.dao.LibraryReservationDAO;
import std.libraryReservations.dao.LibraryReservationsBookDAO;
import std.libraryReservations.dao.LibraryReservationsCustomerDAO;
import std.libraryReservations.dto.CreateReservationDTO;
import std.libraryReservations.dto.LibraryBookForReservationDTO;
import std.libraryReservations.dto.LibraryBuildingForReservationDTO;
import std.libraryReservations.dto.LibraryCustomerForReservationDTO;
import std.libraryReservations.dto.LibraryRoleForReservationDTO;
import std.libraryReservations.dto.ReservationDTO;
import std.libraryReservations.entities.LibraryBookForReservation;
import std.libraryReservations.entities.LibraryBuildingForReservation;
import std.libraryReservations.entities.LibraryCustomerForReservation;
import std.libraryReservations.entities.LibraryRoleForReservation;
import std.libraryReservations.entities.Reservation;
import std.libraryReservations.exceptions.AlreadyReservedByCustomerException;
import std.libraryReservations.exceptions.ReservationsListException;

@Service
public class LibraryReservationServiceImpl implements LibraryReservationService {

    @Autowired
    private LibraryReservationDAO libraryReservationDAO;

    @Autowired
    private LibraryReservationsCustomerDAO libraryReservationsCustomerDAO;

    @Autowired
    private LibraryReservationsBookDAO libraryReservationsBookDAO;

    private ModelMapper modelMapper = new ModelMapper();
    private Reservation reservation = new Reservation();
    private ReservationDTO reservationDTO = new ReservationDTO();
    private LibraryBookForReservation libraryBookForReservation = new LibraryBookForReservation();
    private LibraryBookForReservationDTO libraryBookForReservationDTO = new LibraryBookForReservationDTO();
    private LibraryCustomerForReservation libraryCustomerForReservation = new LibraryCustomerForReservation();
    private LibraryCustomerForReservationDTO libraryCustomerForReservationDTO = new LibraryCustomerForReservationDTO();
    private LibraryBuildingForReservation libraryBuildingForReservation = new LibraryBuildingForReservation();
    private LibraryBuildingForReservationDTO libraryBuildingForReservationDTO = new LibraryBuildingForReservationDTO();
    private LibraryRoleForReservation libraryRoleForReservation = new LibraryRoleForReservation();
    private LibraryRoleForReservationDTO libraryRoleForReservationDTO = new LibraryRoleForReservationDTO();

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
	Reservation reservation = new Reservation();
	LibraryCustomerForReservation customer = customer(customerEmail);
	LibraryBookForReservation book = book(bookId);
	reservation.setBook(book);
	reservation.setCustomer(customer);
	reservation.setPriority(priority(book.getTitle(), customer.getId()));
	reservation.setCanceledStatus(false);
	libraryReservationDAO.saveAndFlush(reservation);
	book.setNumberOfReservations(book.getNumberOfReservations() + 1);
	libraryReservationsBookDAO.saveAndFlush(book);

    }

    private Integer priority(String bookTitle, Integer customerId) {
	List<ReservationDTO> list = getAllBookReservations(bookTitle);
	if (list.isEmpty()) {
	    return 1;
	} else {
	    if (!checkCustomerReservation(list, customerId)) {
		throw new AlreadyReservedByCustomerException();
	    }
	    return list.size() + 1;
	}

    }

    private LibraryCustomerForReservation customer(String eMail) {
	if (libraryReservationsCustomerDAO.findByCustomerEmail(eMail).isPresent()) {
	    return libraryReservationsCustomerDAO.findByCustomerEmail(eMail).get();
	} else {
	    throw new NotFoundException("Reservation service: customer not found");
	}
    }

    private LibraryBookForReservation book(Integer id) {
	if (libraryReservationsBookDAO.findOneById(id).isPresent()) {
	    return libraryReservationsBookDAO.findOneById(id).get();
	} else {
	    throw new NotFoundException("Reservation service: book not found");
	}
    }

    private Boolean checkCustomerReservation(List<ReservationDTO> list, Integer customerId) {
	return list.stream().filter(o -> o.getCustomer().getId() == customerId).collect(Collectors.toList()).isEmpty();
    }

    @Override
    public List<ReservationDTO> getAllCustomerReservations(String customerEmail) {
	return libraryReservationDAO.findByCustomerCustomerEmailAndCanceledStatusFalse(customerEmail).stream()
		.map(o -> mapReservationToDTO(o)).collect(Collectors.toList());
    }

    private ReservationDTO mapReservationToDTO(Reservation ent) {
	ReservationDTO dto = new ReservationDTO();
	dto = reservationToReservationDTO(ent);
	dto.setBuildingName(ent.getBook().getLibraryBuilding().getName());
	return dto;
    }

    @Override
    public void cancelReservation(Integer reservationId) {
	if (libraryReservationDAO.findById(reservationId).isPresent()) {
	    Reservation reservation = libraryReservationDAO.findById(reservationId).get();
	    reservation.setCanceledStatus(true);
	    LibraryBookForReservation book = reservation.getBook();
	    book.setNumberOfReservations(book.getNumberOfReservations() - 1);
	    if (book.getNumberOfReservations() < 0) {
		book.setNumberOfReservations(0);
	    }
	    libraryReservationDAO.saveAndFlush(reservation);
	    libraryReservationsBookDAO.saveAndFlush(book);
	} else {
	    throw new NotFoundException("Reservation service : reservation to cancel not found");
	}
    }

    private Reservation reservationDTOToReservation(CreateReservationDTO dto) {
	return (Reservation) mapper(dto, reservation);
    }

    private ReservationDTO reservationToReservationDTO(Reservation reservation) {
	return (ReservationDTO) mapper(reservation, reservationDTO);
    }

    private LibraryBookForReservation bookDTOToEntity(LibraryBookForReservationDTO dto) {
	return (LibraryBookForReservation) mapper(dto, libraryBookForReservation);
    }

    private LibraryBookForReservationDTO bookEntityToDTO(LibraryBookForReservation ent) {
	return (LibraryBookForReservationDTO) mapper(ent, libraryBookForReservationDTO);
    }

    private LibraryCustomerForReservation customerDTOToEntity(LibraryCustomerForReservationDTO dto) {
	return (LibraryCustomerForReservation) mapper(dto, libraryCustomerForReservation);
    }

    private LibraryCustomerForReservationDTO customerEntityToDTO(LibraryCustomerForReservation ent) {
	return (LibraryCustomerForReservationDTO) mapper(ent, libraryCustomerForReservationDTO);
    }

    private LibraryBuildingForReservation buildingDTOToEntity(LibraryBuildingForReservationDTO dto) {
	return (LibraryBuildingForReservation) mapper(dto, libraryBuildingForReservation);
    }

    private LibraryBuildingForReservationDTO buildingEntityToDTO(LibraryBuildingForReservation ent) {
	return (LibraryBuildingForReservationDTO) mapper(ent, libraryBuildingForReservationDTO);
    }

    private LibraryRoleForReservation roleDTOToEntity(LibraryRoleForReservationDTO dto) {
	return (LibraryRoleForReservation) mapper(dto, libraryRoleForReservation);
    }

    private LibraryRoleForReservationDTO roleEntityToDTO(LibraryRoleForReservation ent) {
	return (LibraryRoleForReservationDTO) mapper(ent, libraryRoleForReservationDTO);
    }

    private Object mapper(Object source, Object destination) {
	return modelMapper.map(source, destination.getClass());
    }

}

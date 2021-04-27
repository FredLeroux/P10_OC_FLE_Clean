package std.libraryReservations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.joda.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import serviceExtended.ReservationServiceImplForTest;
import std.libraryReservations.dao.LibraryReservationDAO;
import std.libraryReservations.dao.LibraryReservationsBookDAO;
import std.libraryReservations.dao.LibraryReservationsCustomerDAO;
import std.libraryReservations.dao.LibraryResevationLoanDAO;
import std.libraryReservations.dto.ReservationDTO;
import std.libraryReservations.entities.LibraryBookForReservation;
import std.libraryReservations.entities.LibraryBuildingForReservation;
import std.libraryReservations.entities.LibraryCustomerForReservation;
import std.libraryReservations.entities.LibraryLoanForReservation;
import std.libraryReservations.entities.Reservation;
import std.libraryReservations.exceptions.AlreadyReservedOrLoanedByCustomerException;
import std.libraryReservations.exceptions.NotFoundInDataBaseException;
import std.libraryReservations.exceptions.ReservationsListException;

@TestPropertySource(locations = "/application-unitTest.properties")
@SpringBootTest(classes = { Reservation.class })
@ExtendWith(MockitoExtension.class)
class LibraryReservationsApplicationTests {

    @MockBean
    private LibraryReservationDAO libraryReservationDAO;

    @MockBean
    private LibraryReservationsCustomerDAO libraryReservationsCustomerDAO;

    @MockBean
    private LibraryResevationLoanDAO libraryReservationLoanDAO;

    @MockBean
    private LibraryReservationsBookDAO libraryReservationsBookDAO;

    private Validator validator;
    private Reservation reservation;
    private Optional<Reservation> optReservation;
    private LibraryBookForReservation bookDb;
    private LibraryBookForReservation bookTest;
    private Optional<LibraryBookForReservation> optBook;
    private LibraryCustomerForReservation customerDb;
    private LibraryCustomerForReservation customerTest;
    private Optional<LibraryCustomerForReservation> optCustomer;
    private LibraryLoanForReservation loanDb;
    private Optional<LibraryLoanForReservation> optLoan;
    private List<ReservationDTO> listReservationDTO;
    private List<Reservation> listReservation;

    @InjectMocks
    private ReservationServiceImplForTest service = new ReservationServiceImplForTest();

    @BeforeEach
    private void validator() {
	ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	validator = factory.getValidator();

    }

    @BeforeEach
    private void initObject() {
	bookDb = new LibraryBookForReservation();
	bookTest = new LibraryBookForReservation();
	optBook = Optional.of(bookDb);
	customerDb = new LibraryCustomerForReservation();
	customerTest = new LibraryCustomerForReservation();
	optCustomer = Optional.of(customerDb);
	loanDb = new LibraryLoanForReservation();
	optLoan = Optional.of(loanDb);
	customerDb.setId(999);
	bookDb.setTitle("bookTitle");
	bookDb.setId(1);
	bookDb.setLibraryBuilding(new LibraryBuildingForReservation(1, "buildinName1"));
	reservation = new Reservation(1, null, false, 1, bookDb, customerDb);
	optReservation = Optional.of(reservation);
	listReservationDTO = new ArrayList<ReservationDTO>();
	listReservationDTO.add(new ReservationDTO(1, null, false, 1, "building", null, false, bookDb, customerDb));
	listReservationDTO.add(new ReservationDTO(2, null, false, 1, "building", null, false, bookDb, customerDb));
	listReservationDTO.add(new ReservationDTO(3, null, false, 1, "building", null, false, bookDb, customerDb));
	listReservationDTO.add(new ReservationDTO(4, null, false, 1, "building", null, false, bookDb, customerDb));
	listReservation = new ArrayList<Reservation>();
	listReservation.add(new Reservation(1, null, false, 1, bookDb, customerDb));
	listReservation.add(new Reservation(2, null, false, 2, bookDb, customerDb));
	listReservation.add(new Reservation(3, null, false, 3, bookDb, customerDb));
	listReservation.add(new Reservation(4, null, false, 4, bookDb, customerDb));
    }

    @BeforeEach
    public void resetDaoAndService() {
	reset(libraryReservationDAO);
	reset(libraryReservationsBookDAO);
    }

    @Nested
    @DisplayName(value = "Reservation constraints")
    public class reservationConstraintsTest {

	@Test
	public void reservationConstraintsPass() {
	    assertThat(constrainCheck(reservation)).isTrue();
	}

	@Test
	public void reservationConstraintsFail() {
	    reservation = new Reservation();
	    assertThat(constrainCheck(reservation)).isFalse();
	}

	@Test
	public void reservationCancelStatusFail() {
	    reservation.setCanceledStatus(null);
	    assertThat(constrainCheck(reservation)).isFalse();
	}

	@Test
	public void reservationPriorityFail() {
	    reservation.setPriority(null);
	    assertThat(constrainCheck(reservation)).isFalse();
	}

	@Test
	public void reservationBookFail() {
	    reservation.setBook(null);
	    assertThat(constrainCheck(reservation)).isFalse();
	}

	@Test
	public void reservationCustomerFail() {
	    reservation.setCustomer(null);
	    assertThat(constrainCheck(reservation)).isFalse();
	}

	private Boolean constrainCheck(Reservation reservation) {
	    Set<ConstraintViolation<Reservation>> validation = validator.validate(reservation);
	    return validation.isEmpty();
	}

    }

    @Nested
    @DisplayName("Create reservation process tests")
    public class CreateReservationsTest {

	@Test
	public void priorityTestFunctionListEmpty() {
	    listReservationDTO.clear();
	    assertThat(service.priority(listReservationDTO, 1)).isEqualTo(1);
	}

	@Test
	public void priorityTestDunctionListNotEmpty() {
	    assertThat(service.priority(listReservationDTO, 1)).isEqualTo(listReservationDTO.size() + 1);
	}

	@Test
	public void priorityTestThrowCustomerEception() {
	    assertThatThrownBy(() -> service.priority(listReservationDTO, 999))
		    .isInstanceOf(AlreadyReservedOrLoanedByCustomerException.class);
	}

	@Test
	public void getAllBookReservationsTest() {
	    when(libraryReservationDAO.findByBookTitleAndCanceledStatusFalse(bookTest.getTitle())).thenReturn(null);
	    assertThatThrownBy(() -> service.getAllBookReservations(bookTest.getTitle()))
		    .isInstanceOf(ReservationsListException.class);
	}

	@Test
	public void reservationTestPass() {
	    customerTest.setId(10);
	    bookTest.setTitle("title");
	    when(libraryReservationDAO.findByBookTitleAndCanceledStatusFalse(bookTest.getTitle()))
		    .thenReturn(listReservation);
	    assertThatCode(() -> service.reservation(bookTest, customerTest)).doesNotThrowAnyException();
	    assertThat(service.reservation(bookTest, customerTest).getPriority()).isEqualTo(listReservation.size() + 1);
	    assertThat(service.reservation(bookTest, customerTest).getCanceledStatus()).isFalse();
	    assertThat(service.reservation(bookTest, customerTest).getBook()).isEqualTo(bookTest);
	    assertThat(service.reservation(bookTest, customerTest).getCustomer()).isEqualTo(customerTest);
	}

	@Test
	public void reservationTestPassOnReservationListEmpty() {
	    customerTest.setId(10);
	    bookTest.setTitle("title");
	    listReservation.clear();
	    when(libraryReservationDAO.findByBookTitleAndCanceledStatusFalse(bookTest.getTitle()))
		    .thenReturn(listReservation);
	    assertThat(service.reservation(bookTest, customerTest).getPriority()).isEqualTo(1);
	}

	@Test
	public void reservationTestFail() {
	    customerTest.setId(999);
	    bookTest.setTitle("title");
	    when(libraryReservationDAO.findByBookTitleAndCanceledStatusFalse(bookTest.getTitle()))
		    .thenReturn(listReservation);
	    assertThatThrownBy(() -> service.reservation(bookTest, customerTest))
		    .isInstanceOf(AlreadyReservedOrLoanedByCustomerException.class);
	}

	@Test
	public void customerTestPass() {
	    when(libraryReservationsCustomerDAO.findByCustomerEmail("eMail")).thenReturn(optCustomer);
	    assertThatCode(() -> service.customer("eMail")).doesNotThrowAnyException();
	}

	@Test
	public void customerTestFail() {
	    when(libraryReservationsCustomerDAO.findByCustomerEmail("eMail")).thenReturn(Optional.empty());
	    assertThatThrownBy(() -> service.customer("eMail")).isInstanceOf(NotFoundInDataBaseException.class)
		    .hasMessage("Reservation service: customer not found");
	}

	@Test
	public void bookTestPass() {
	    when(libraryReservationsBookDAO.findOneById(1)).thenReturn(optBook);
	    assertThatCode(() -> service.book(1)).doesNotThrowAnyException();
	}

	@Test
	public void bookTestFail() {
	    when(libraryReservationsBookDAO.findOneById(1)).thenReturn(Optional.empty());
	    assertThatThrownBy(() -> service.book(1)).isInstanceOf(NotFoundInDataBaseException.class)
		    .hasMessage("Reservation service: book not found");
	    ;
	}

	@ParameterizedTest
	@ValueSource(ints = { 0, 1, 2, 3 })
	public void updateBookNbReservationTest(Integer ints) {
	    bookTest.setNumberOfReservations(ints);
	    Integer numberOfReservation = bookTest.getNumberOfReservations();
	    assertThat(service.updateBookNbReservation(bookTest).getNumberOfReservations())
		    .isEqualTo(numberOfReservation + 1);
	}

	@Test
	public void createReservationTestFailCustomerNotFound() {
	    bookDb.setNumberOfReservations(1);
	    when(libraryReservationsCustomerDAO.findByCustomerEmail("eMail")).thenReturn(Optional.empty());
	    when(libraryReservationsBookDAO.findOneById(1)).thenReturn(optBook);
	    when(libraryReservationLoanDAO.findByBookTitleAndCustomerIdAndReturnedFalse(bookDb.getTitle(),
		    customerDb.getId())).thenReturn(optLoan);
	    assertThatThrownBy(() -> service.createReservation(1, "eMail"))
		    .isInstanceOf(NotFoundInDataBaseException.class);
	    verify(libraryReservationDAO, times(0)).saveAndFlush(ArgumentMatchers.any());
	    verify(libraryReservationsBookDAO, times(0)).saveAndFlush(ArgumentMatchers.any());
	}

	@Test
	public void createReservationTestPass() {
	    bookDb.setNumberOfReservations(1);
	    when(libraryReservationsCustomerDAO.findByCustomerEmail("eMail")).thenReturn(optCustomer);
	    when(libraryReservationsBookDAO.findOneById(1)).thenReturn(optBook);
	    when(libraryReservationLoanDAO.findByBookTitleAndCustomerIdAndReturnedFalse(bookDb.getTitle(),
		    customerDb.getId())).thenReturn(Optional.empty());
	    assertThatCode(() -> service.createReservation(1, "eMail")).doesNotThrowAnyException();
	    verify(libraryReservationDAO, times(1)).saveAndFlush(ArgumentMatchers.any());
	    verify(libraryReservationsBookDAO, times(1)).saveAndFlush(ArgumentMatchers.any());
	}

	@Test
	public void createReservationTestFailAlreadyReservedOrLoaned() {
	    bookDb.setNumberOfReservations(1);
	    when(libraryReservationsCustomerDAO.findByCustomerEmail("eMail")).thenReturn(optCustomer);
	    when(libraryReservationsBookDAO.findOneById(1)).thenReturn(optBook);
	    when(libraryReservationLoanDAO.findByBookTitleAndCustomerIdAndReturnedFalse(bookDb.getTitle(),
		    customerDb.getId())).thenReturn(optLoan);
	    assertThatThrownBy(() -> service.createReservation(1, "eMail"))
		    .isInstanceOf(AlreadyReservedOrLoanedByCustomerException.class);
	    verify(libraryReservationDAO, times(0)).saveAndFlush(ArgumentMatchers.any());
	    verify(libraryReservationsBookDAO, times(0)).saveAndFlush(ArgumentMatchers.any());
	}

    }

    @Nested
    @DisplayName(value = "Cancel reservation process Tests")
    public class CancelReservationTests {

	@Test
	public void cancelReservationUpdateTestPass() {
	    when(libraryReservationDAO.findById(1)).thenReturn(optReservation);
	    assertThatCode(() -> service.cancelReservationUpdate(1)).doesNotThrowAnyException();
	}

	@Test
	public void cancelReservationUpdateTestFail() {
	    when(libraryReservationDAO.findById(1)).thenReturn(Optional.empty());
	    assertThatThrownBy(() -> service.cancelReservationUpdate(1)).isInstanceOf(NotFoundInDataBaseException.class)
		    .hasMessage("Reservation service : reservation to cancel not found");
	}

	@ParameterizedTest
	@ValueSource(ints = { 1, 2, 3, 4 })
	public void cancelReservationUpdateBookTestPosValues(Integer ints) {
	    bookTest.setNumberOfReservations(ints);
	    assertThat(service.cancelReservationUpdateBook(bookTest).getNumberOfReservations()).isEqualTo(ints - 1);
	}

	@ParameterizedTest
	@ValueSource(ints = { -1, 0 })
	public void cancelReservationUpdateBookTestNegValues(Integer ints) {
	    bookTest.setNumberOfReservations(ints);
	    assertThat(service.cancelReservationUpdateBook(bookTest).getNumberOfReservations()).isEqualTo(0);
	}

	@Test
	public void cancelReservationTestPass() {
	    bookTest.setNumberOfReservations(1);
	    reservation.setBook(bookTest);
	    when(libraryReservationDAO.findById(1)).thenReturn(optReservation);
	    assertThatCode(() -> service.cancelReservation(1)).doesNotThrowAnyException();
	    verify(libraryReservationDAO, times(1)).saveAndFlush(ArgumentMatchers.any());
	    verify(libraryReservationsBookDAO, times(1)).saveAndFlush(ArgumentMatchers.any());
	}

	@Test
	public void cancelReservationTestFail() {
	    when(libraryReservationDAO.findById(1)).thenReturn(Optional.empty());
	    assertThatThrownBy(() -> service.cancelReservation(1)).isInstanceOf(NotFoundInDataBaseException.class);
	    verify(libraryReservationDAO, times(0)).saveAndFlush(ArgumentMatchers.any());
	    verify(libraryReservationsBookDAO, times(0)).saveAndFlush(ArgumentMatchers.any());
	}

    }

    @Nested
    @DisplayName(value = "Send customer to notified info process test")
    public class customerToNotifiedTests {

	@Test
	public void customerToNotifiedTestPass() {
	    customerTest.setCustomerEmail("eMail");
	    bookTest.setTitle("title");
	    reservation.setCustomer(customerTest);
	    bookTest.setLibraryBuilding(new LibraryBuildingForReservation(1, "BuildingName"));
	    reservation.setBook(bookTest);
	    reservation.setId(10);
	    when(libraryReservationDAO.findByBookIdAndPriorityAndCanceledStatusFalse(ArgumentMatchers.anyInt(),
		    ArgumentMatchers.anyInt())).thenReturn(optReservation);
	    assertThatCode(() -> service.customerToNotified(1, 1)).doesNotThrowAnyException();
	    assertThat(service.customerToNotified(1, 1).getReference()).isEqualTo(10);
	    assertThat(service.customerToNotified(1, 1).getCustomerEmail()).isEqualTo("eMail");
	    assertThat(service.customerToNotified(1, 1).getBookTitle()).isEqualTo("title");
	    assertThat(service.customerToNotified(1, 1).getBuilding()).isEqualTo("BuildingName");
	}

	@Test
	public void customerToNotifiedTestreturnNUll() {
	    when(libraryReservationDAO.findByBookIdAndPriorityAndCanceledStatusFalse(ArgumentMatchers.anyInt(),
		    ArgumentMatchers.anyInt())).thenReturn(Optional.empty());
	    assertThatCode(() -> service.customerToNotified(1, 1)).doesNotThrowAnyException();
	    assertThat(service.customerToNotified(1, 1)).isNull();
	}

    }

    @Nested
    @DisplayName(value = "Customer reservations list tests")
    public class CustomerReservationsListTests {

	private LibraryLoanForReservation loan;
	private List<LibraryLoanForReservation> loans;
	private LibraryBookForReservation bookTest1;
	private LibraryBookForReservation bookTest2;

	@BeforeEach
	public void initCustomerReservationsListTestsList() {
	    bookTest1 = new LibraryBookForReservation();
	    bookTest2 = new LibraryBookForReservation();
	    bookTest1.setId(1);
	    bookTest1.setLibraryBuilding(new LibraryBuildingForReservation(1, "buildinName1"));
	    bookTest2.setId(2);
	    bookTest1.setTitle("title1");
	    reservation.setBook(bookTest1);
	    loans = new ArrayList<LibraryLoanForReservation>();
	    loans.add(new LibraryLoanForReservation(1, "2021-04-22", false, false, bookTest1, customerDb));
	    loans.add(
		    new LibraryLoanForReservation(1, LocalDate.now().toString(), false, false, bookTest2, customerDb));
	}

	@Test
	public void reservationBooksIdTest() {
	    service.reservationBooksId(listReservation).forEach(o -> assertThat(o.equals(1)));
	}

	@Test
	public void reservationBooksIdTestEmpty() {
	    listReservation.clear();
	    assertThat(service.reservationBooksId(listReservation)).isEmpty();
	}

	@Test
	public void linkedloanTest() {
	    when(libraryReservationLoanDAO
		    .findByBookIdInAndAndReturnedFalse(service.reservationBooksId(ArgumentMatchers.anyList())))
			    .thenReturn(loans);
	    assertThat(service.linkedLoan(1, service.reservationsLinkedLoan(listReservation)).getBook().getTitle())
		    .isEqualTo("title1");

	}

	@Test
	public void linkedloanTestFail() {
	    loans.clear();
	    when(libraryReservationLoanDAO
		    .findByBookIdInAndAndReturnedFalse(service.reservationBooksId(ArgumentMatchers.anyList())))
			    .thenReturn(loans);
	    assertThatThrownBy(() -> service.linkedLoan(1, service.reservationsLinkedLoan(listReservation)))
		    .isInstanceOf(NullPointerException.class)
		    .hasMessage("Reservation service: reservation Linked loan return null");

	}

	@Test
	public void mapReservationToDTO() {
	    when(libraryReservationLoanDAO
		    .findByBookIdInAndAndReturnedFalse(service.reservationBooksId(ArgumentMatchers.anyList())))
			    .thenReturn(loans);
	    assertThat(service.mapReservationToDTO(reservation, service.reservationsLinkedLoan(listReservation))
		    .getBuildingName()).isEqualTo("buildinName1");
	    assertThat(service.mapReservationToDTO(reservation, service.reservationsLinkedLoan(listReservation))
		    .getReturnDate()).isEqualTo("2021-04-22");
	    assertThat(service.mapReservationToDTO(reservation, service.reservationsLinkedLoan(listReservation))
		    .getPostpone()).isFalse();

	}

	@Test
	public void getAllCustomerReservationsTest() {
	    listReservation.add(reservation);
	    when(libraryReservationDAO.findByCustomerCustomerEmailAndCanceledStatusFalse(ArgumentMatchers.anyString()))
		    .thenReturn(listReservation);
	    when(libraryReservationLoanDAO
		    .findByBookIdInAndAndReturnedFalse(service.reservationBooksId(ArgumentMatchers.anyList())))
			    .thenReturn(loans);
	    assertThat(service.getAllCustomerReservations("mail")).isNotEmpty();

	}

    }

}

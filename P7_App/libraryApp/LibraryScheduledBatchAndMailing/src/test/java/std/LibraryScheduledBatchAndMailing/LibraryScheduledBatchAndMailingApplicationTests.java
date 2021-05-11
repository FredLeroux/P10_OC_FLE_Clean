package std.LibraryScheduledBatchAndMailing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

import std.LibraryScheduledBatchAndMailing.dao.CustomerBatchDAO;
import std.LibraryScheduledBatchAndMailing.dao.LibraryBookBatchDAO;
import std.LibraryScheduledBatchAndMailing.dao.LoanBatchDAO;
import std.LibraryScheduledBatchAndMailing.dao.ReservationBatchDAO;
import std.LibraryScheduledBatchAndMailing.dto.LoanBatchMailInfoDTO;
import std.LibraryScheduledBatchAndMailing.dto.ReservationToNotifiedInfoDTO;
import std.LibraryScheduledBatchAndMailing.entities.CustomerBatch;
import std.LibraryScheduledBatchAndMailing.entities.LibraryBookBatch;
import std.LibraryScheduledBatchAndMailing.entities.LibraryBuildingBatch;
import std.LibraryScheduledBatchAndMailing.entities.LoanBatch;
import std.LibraryScheduledBatchAndMailing.entities.ReservationBatch;
import std.LibraryScheduledBatchAndMailing.exceptions.BookNotFoundException;
import std.LibraryScheduledBatchAndMailing.exceptions.CustomerNotFoundException;
import std.LibraryScheduledBatchAndMailing.serviceExtended.BatchServiceImplForTest;

@SpringBootTest(classes = BatchServiceImplForTest.class)
@TestPropertySource(locations = "/bootstrapTest.properties")
@ExtendWith(MockitoExtension.class)
class LibraryScheduledBatchAndMailingApplicationTests {

    @MockBean
    private LoanBatchDAO loanBatchDao;

    @MockBean
    private CustomerBatchDAO customerBatchDao;

    @MockBean
    private LibraryBookBatchDAO libraryBookBatchDao;

    @MockBean
    private ReservationBatchDAO reservationBatchDAO;

    @InjectMocks
    private BatchServiceImplForTest service;

    private ReservationBatch reservation;
    private Optional<ReservationBatch> optReservation;
    private CustomerBatch customer;
    private Optional<CustomerBatch> optCustomer;
    private LibraryBookBatch book;
    private LibraryBookBatch bookTest1;
    private LibraryBookBatch bookTest2;
    private LibraryBookBatch bookTest3;
    private Optional<LibraryBookBatch> optBook;
    private List<ReservationBatch> listReservations;

    @BeforeEach
    public void initObject() {
	reset(reservationBatchDAO);
	reservation = new ReservationBatch();
	optReservation = Optional.of(reservation);
	listReservations = new ArrayList<ReservationBatch>();
	customer = new CustomerBatch();
	book = new LibraryBookBatch();
	customer.setCustomerEmail("Email");
	book.setTitle("Title");
	book.setLibraryBuilding(new LibraryBuildingBatch(1, "buildingName"));
	reservation.setId(1);
	reservation.setCustomer(customer);
	reservation.setBook(book);
	listReservations.add(reservation);
	listReservations.add(reservation);
	listReservations.add(reservation);
	listReservations.add(reservation);
	listReservations.add(reservation);

    }

    @Test
    public void setNotificationDateToNowTest() {
	assertThat(service.setNotificationDateToNow(reservation).getNotificationDate())
		.isEqualTo(LocalDate.now().toString());
    }

    @Test
    public void updateNotificationDateOnListTestPass() {
	service.updateNotificationDate(listReservations);
	listReservations.forEach(o -> assertThat(service.setNotificationDateToNow(reservation).getNotificationDate())
		.isEqualTo(LocalDate.now().toString()));
    }

    @Test
    public void updateNotificationDateTestPass() {
	when(reservationBatchDAO.findByCustomerCustomerEmailAndBookTitleAndCanceledStatusFalse(
		ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(optReservation);
	assertThatCode(() -> service.updateNotificationDate("customerEmail", "bookTitle")).doesNotThrowAnyException();
	verify(reservationBatchDAO, times(1)).saveAndFlush(ArgumentMatchers.any(ReservationBatch.class));
    }

    @Test
    public void updateNotificationDateTestFail() {
	when(reservationBatchDAO.findByCustomerCustomerEmailAndBookTitleAndCanceledStatusFalse(
		ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(Optional.empty());
	assertThatThrownBy(() -> service.updateNotificationDate("customerEmail", "bookTitle"))
		.isInstanceOf(NullPointerException.class).hasMessage("update notification date fail null reservation");
	verify(reservationBatchDAO, times(0)).saveAndFlush(ArgumentMatchers.any(ReservationBatch.class));
    }

    @Test
    public void reservationToNotifiedInfoDTOTest() {

	assertThat(service.reservationToNotifiedInfoDTO(reservation)).isInstanceOf(ReservationToNotifiedInfoDTO.class);
	assertThat(service.reservationToNotifiedInfoDTO(reservation).getReference()).isEqualTo(1);
	assertThat(service.reservationToNotifiedInfoDTO(reservation).getCustomerEmail()).isEqualTo("Email");
	assertThat(service.reservationToNotifiedInfoDTO(reservation).getBookTitle()).isEqualTo("Title");
	assertThat(service.reservationToNotifiedInfoDTO(reservation).getBuilding()).isEqualTo("buildingName");
    }

    @Test
    public void reservationsToCancelInfoTestPass() {
	service.reservationsToCancelInfo(listReservations)
		.forEach(o -> assertThat(o).isInstanceOf(ReservationToNotifiedInfoDTO.class));
    }

    @Test
    public void reservationsToNotifiedBookAvailableInfoTestPass() {
	service.reservationsToNotifiedBookAvailableInfo(listReservations)
		.forEach(o -> assertThat(o).isInstanceOf(ReservationToNotifiedInfoDTO.class));
    }

    @Test
    public void reservationsToNotifiedBookAvailableInfoNull() {
	listReservations.clear();
	assertThat(service.reservationsToNotifiedBookAvailableInfo(listReservations)).isEmpty();
    }

    @Test
    public void reservationsToCancelInfoTestNull() {
	listReservations.clear();
	assertThat(service.reservationsToCancelInfo(listReservations)).isNull();
    }

    @ParameterizedTest
    @ValueSource(ints = { 2, 3, 4 })
    public void reservationsToCancelListDelayExceededTest(Integer ints) {
	listReservations.clear();
	String lateDate = LocalDate.now().minusDays((ints + 1)).toString();
	String dayPass = LocalDate.now().minusDays((ints)).toString();
	String dayPass1 = LocalDate.now().minusDays((ints - 1)).toString();
	listReservations.add(new ReservationBatch(1, lateDate, false, 1, book, customer));
	listReservations.add(new ReservationBatch(2, dayPass1, false, 1, book, customer));
	listReservations.add(new ReservationBatch(3, lateDate, false, 1, book, customer));
	listReservations.add(new ReservationBatch(4, lateDate, false, 1, book, customer));
	listReservations.add(new ReservationBatch(5, dayPass, false, 1, book, customer));
	listReservations.add(new ReservationBatch(6, lateDate, false, 1, book, customer));
	assertThat(service.reservationsToCancelListDelayExceeded(listReservations, (long) ints).size()).isEqualTo(4);
    }

    @Test
    public void reservationsToCancelListDelayExceededTestEmpty() {
	listReservations.clear();
	assertThat(service.reservationsToCancelListDelayExceeded(listReservations, (long) 2).size()).isEqualTo(0);
    }

    @Test
    public void reservationsToCheckCancelationTest() {
	listReservations.clear();
	String date = LocalDate.now().toString();
	listReservations.add(new ReservationBatch(1, date, false, 1, book, customer));
	listReservations.add(new ReservationBatch(2, null, false, 1, book, customer));
	listReservations.add(new ReservationBatch(3, null, false, 1, book, customer));
	listReservations.add(new ReservationBatch(4, date, false, 1, book, customer));
	listReservations.add(new ReservationBatch(5, null, false, 1, book, customer));
	listReservations.add(new ReservationBatch(6, date, false, 1, book, customer));
	assertThat(service.reservationsToCheckCancelation(listReservations).size()).isEqualTo(3);
    }

    @Test
    public void reservationsToCheckCancelationTestEmpty() {
	listReservations.clear();
	assertThat(service.reservationsToCheckCancelation(listReservations).size()).isEqualTo(0);
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    public void updateReservationsCancelStatusTest(Boolean booleans) {
	service.updateReservationsCancelStatus(listReservations, booleans)
		.forEach(o -> assertThat(o.getCanceledStatus().equals(booleans)));
    }

    @Test
    public void updateReservationsCancelStatusTestEmpty() {
	listReservations.clear();
	assertThat(service.updateReservationsCancelStatus(listReservations, true));

    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 2, 3 })
    public void updateReservationsPriorityTest(Integer ints) {
	service.updateReservationsPriority(listReservations, ints);
	listReservations.forEach(o -> assertThat(o.getPriority()).isEqualTo(ints));
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 2, 3 })
    public void updateReservationsPriorityTestEmpty(Integer ints) {
	listReservations.clear();
	service.updateReservationsPriority(listReservations, ints);
	listReservations.forEach(o -> assertThat(o.getPriority()).isNull());
    }

    @Test
    public void reservationsListToUpdatePriorityTest() {
	listReservations.clear();
	List<ReservationBatch> rawList = new ArrayList<>();
	bookTest1 = new LibraryBookBatch();
	bookTest1.setId(1);
	bookTest2 = new LibraryBookBatch();
	bookTest2.setId(2);
	bookTest3 = new LibraryBookBatch();
	bookTest3.setId(3);
	listReservations.add(new ReservationBatch(9, null, false, 2, bookTest1, customer));
	listReservations.add(new ReservationBatch(10, null, false, 3, bookTest1, customer));
	listReservations.add(new ReservationBatch(11, null, false, 2, bookTest2, customer));
	listReservations.add(new ReservationBatch(12, null, false, 3, bookTest2, customer));
	rawList.add(new ReservationBatch(1, null, false, 2, bookTest1, customer));
	rawList.add(new ReservationBatch(2, null, false, 3, bookTest1, customer));
	rawList.add(new ReservationBatch(3, null, false, 4, bookTest1, customer));
	rawList.add(new ReservationBatch(4, null, false, 3, bookTest2, customer));
	rawList.add(new ReservationBatch(5, null, false, 2, bookTest2, customer));
	rawList.add(new ReservationBatch(6, null, false, 4, bookTest2, customer));
	rawList.add(new ReservationBatch(7, null, false, 1, bookTest3, customer));
	rawList.add(new ReservationBatch(8, null, false, 2, bookTest3, customer));
	assertThat(service.reservationsListToUpdatePriority(rawList, listReservations, 1).size()).isEqualTo(2);
    }

    @Test
    public void reservationsListToUpdatePriorityTestEmpty() {
	listReservations.clear();
	List<ReservationBatch> rawList = new ArrayList<>();
	bookTest1 = new LibraryBookBatch();
	bookTest1.setTitle("canceledBook");
	bookTest2 = new LibraryBookBatch();
	bookTest2.setTitle("canceledBook1");
	bookTest3 = new LibraryBookBatch();
	bookTest3.setTitle("notCanceledBook");
	rawList.add(new ReservationBatch(1, null, false, 2, bookTest1, customer));
	rawList.add(new ReservationBatch(2, null, false, 3, bookTest1, customer));
	rawList.add(new ReservationBatch(3, null, false, 4, bookTest1, customer));
	rawList.add(new ReservationBatch(4, null, false, 3, bookTest2, customer));
	rawList.add(new ReservationBatch(5, null, false, 2, bookTest2, customer));
	rawList.add(new ReservationBatch(6, null, false, 4, bookTest2, customer));
	rawList.add(new ReservationBatch(7, null, false, 1, bookTest3, customer));
	rawList.add(new ReservationBatch(8, null, false, 2, bookTest3, customer));
	assertThat(service.reservationsListToUpdatePriority(rawList, listReservations, 1)).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3 })
    public void canceledReservationLinkedBooksTitlesTest(Integer ints) {
	listReservations.clear();
	bookTest1 = new LibraryBookBatch();
	bookTest1.setId(ints);
	listReservations.add(new ReservationBatch(1, null, false, 1, bookTest1, customer));
	listReservations.add(new ReservationBatch(2, null, false, 1, bookTest1, customer));
	listReservations.add(new ReservationBatch(3, null, false, 1, bookTest1, customer));
	listReservations.add(new ReservationBatch(4, null, false, 1, bookTest1, customer));
	service.canceledReservationLinkedBooksId(listReservations).forEach(o -> assertThat(o).isEqualTo(ints));
    }

    @Test
    public void canceledReservationLinkedBooksTitlesTestEmpty() {
	listReservations.clear();
	assertThat(service.canceledReservationLinkedBooksId(listReservations)).isEmpty();
    }

    @Nested
    @DisplayName(value = "update reservations tests")
    public class updateReservationsTests {

	private List<ReservationBatch> cancel;
	private List<ReservationBatch> priority;
	private List<ReservationBatch> save;

	@BeforeEach
	public void listInit() {
	    reset(reservationBatchDAO);
	    cancel = new ArrayList<>();
	    priority = new ArrayList<>();
	    save = new ArrayList<>();
	    cancel.add(new ReservationBatch());
	    cancel.add(new ReservationBatch());
	    cancel.add(new ReservationBatch());
	    priority.add(new ReservationBatch());
	    priority.add(new ReservationBatch());
	    priority.add(new ReservationBatch());
	    save.add(new ReservationBatch());
	    save.add(new ReservationBatch());
	    save.add(new ReservationBatch());
	}

	@Test
	public void reservationsListToSaveTest() {
	    assertThat(service.reservationsListToSave(cancel, priority).size()).isEqualTo(6);
	}

	@Test
	public void reservationsListToSaveTestCancelEmpty() {
	    cancel.clear();
	    assertThat(service.reservationsListToSave(cancel, priority).size()).isEqualTo(3);
	}

	@Test
	public void reservationsListToSaveTestPriorityEmpty() {
	    priority.clear();
	    assertThat(service.reservationsListToSave(cancel, priority).size()).isEqualTo(3);
	}

	@Test
	public void reservationsListToSaveTestEmpty() {
	    cancel.clear();
	    priority.clear();
	    assertThat(service.reservationsListToSave(cancel, priority).size()).isEqualTo(0);
	}

	@Test
	public void updateReservationsTest() {
	    service.updateReservations(save);
	    verify(reservationBatchDAO, times(1)).saveAll(ArgumentMatchers.anyIterable());
	}

	@Test
	public void updateReservationsTestEmpty() {
	    save.clear();
	    service.updateReservations(save);
	    verify(reservationBatchDAO, times(0)).saveAll(ArgumentMatchers.anyIterable());
	}

    }

    @Nested
    @DisplayName(value = "books update tests")

    public class booksUpdateTests {

	private List<LibraryBookBatch> booksList;

	@BeforeEach
	public void booksListInit() {
	    reset(libraryBookBatchDao);
	    booksList = new ArrayList<>();
	    bookTest1 = new LibraryBookBatch();
	    bookTest2 = new LibraryBookBatch();
	    bookTest3 = new LibraryBookBatch();
	    bookTest1.setNumberOfReservations(1);
	    bookTest2.setNumberOfReservations(1);
	    bookTest3.setNumberOfReservations(1);
	    booksList.add(bookTest1);
	    booksList.add(bookTest2);
	    booksList.add(bookTest3);
	    listReservations.add(new ReservationBatch(1, null, false, 1, bookTest1, customer));
	    listReservations.add(new ReservationBatch(2, null, false, 1, bookTest1, customer));
	    listReservations.add(new ReservationBatch(3, null, false, 1, bookTest1, customer));
	    listReservations.add(new ReservationBatch(4, null, false, 1, bookTest1, customer));

	}

	@Test
	public void updateBooksNumberOfreservationTestPos() {
	    service.updateBooksNumberOfreservation(booksList, 1)
		    .forEach(o -> assertThat(o.getNumberOfReservations()).isEqualTo(2));
	}

	@Test
	public void updateBooksNumberOfreservationTestNeg() {
	    service.updateBooksNumberOfreservation(booksList, -1)
		    .forEach(o -> assertThat(o.getNumberOfReservations()).isEqualTo(0));
	}

	@Test
	public void updateBooksNumberOfreservationTestReturnZero() {
	    service.updateBooksNumberOfreservation(booksList, -2)
		    .forEach(o -> assertThat(o.getNumberOfReservations()).isEqualTo(0));
	}

	@Test
	public void updateBooksNumberOfreservationTestEmpty() {
	    booksList.clear();
	    service.updateBooksNumberOfreservation(booksList, -1)
		    .forEach(o -> assertThat(o.getNumberOfReservations()).isEqualTo(0));
	}

	@Test
	public void booksListFromReservationsListTest() {
	    service.booksListFromReservationsList(listReservations).forEach(o -> assertThat(o.equals(bookTest1)));
	    assertThat(service.booksListFromReservationsList(listReservations).size())
		    .isEqualTo(listReservations.size());
	}

	@Test
	public void booksListFromReservationsListTestEmpty() {
	    listReservations.clear();
	    assertThat(service.booksListFromReservationsList(listReservations)).isEmpty();
	}

	@Test
	public void updateBooksTest() {
	    service.updateBooks(booksList);
	    verify(libraryBookBatchDao, times(1)).saveAll(ArgumentMatchers.anyIterable());
	}

	@Test
	public void updateBooksTestEmpty() {
	    booksList.clear();
	    service.updateBooks(booksList);
	    verify(libraryBookBatchDao, times(0)).saveAll(ArgumentMatchers.anyIterable());
	}

    }

    @Test
    public void updateAndSaveReservationAndLinkedBookOnExceedDelayTests() {
	listReservations.clear();
	List<ReservationBatch> rawList = new ArrayList<>();
	bookTest1 = new LibraryBookBatch();
	bookTest1.setNumberOfReservations(1);
	listReservations.add(new ReservationBatch(9, null, false, 2, bookTest1, customer));
	listReservations.add(new ReservationBatch(10, null, false, 3, bookTest1, customer));
	listReservations.add(new ReservationBatch(11, null, false, 2, bookTest1, customer));
	rawList.add(new ReservationBatch(1, null, false, 2, bookTest1, customer));
	rawList.add(new ReservationBatch(2, null, false, 3, bookTest1, customer));
	rawList.add(new ReservationBatch(3, null, false, 4, bookTest1, customer));
	service.updateAndSaveReservationAndLinkedBookOnExceedDelay(listReservations, rawList, -1);
	verify(reservationBatchDAO, times(1)).saveAll(ArgumentMatchers.anyIterable());
	verify(libraryBookBatchDao, times(1)).saveAll(ArgumentMatchers.anyIterable());
	listReservations.clear();
	verify(reservationBatchDAO, times(1)).saveAll(ArgumentMatchers.anyIterable());
	verify(libraryBookBatchDao, times(1)).saveAll(ArgumentMatchers.anyIterable());
	rawList.clear();
	verify(reservationBatchDAO, times(1)).saveAll(ArgumentMatchers.anyIterable());
	verify(libraryBookBatchDao, times(1)).saveAll(ArgumentMatchers.anyIterable());
	reset(reservationBatchDAO);
	reset(libraryBookBatchDao);
	listReservations.clear();
	rawList.clear();
	verify(reservationBatchDAO, times(0)).saveAll(ArgumentMatchers.anyIterable());
	verify(libraryBookBatchDao, times(0)).saveAll(ArgumentMatchers.anyIterable());

    }

    @Test
    public void nextPriorytyNotificationAfterCustomerCancelTest() {
	when(reservationBatchDAO
		.findByBookIdAndBookAvailabilityTrueAndPriorityAndNotificationDateNullAndCanceledStatusFalse(
			ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt())).thenReturn(optReservation);
	assertThat(service.nextPriorytyNotificationAfterCustomerCancel(1, 1)).isNotNull();
	assertThat(service.nextPriorytyNotificationAfterCustomerCancel(1, 1))
		.isInstanceOf(ReservationToNotifiedInfoDTO.class);
    }

    @Test
    public void nextPriorytyNotificationAfterCustomerCancelTestNull() {
	when(reservationBatchDAO
		.findByBookIdAndBookAvailabilityTrueAndPriorityAndNotificationDateNullAndCanceledStatusFalse(
			ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt())).thenReturn(Optional.empty());
	assertThat(service.nextPriorytyNotificationAfterCustomerCancel(1, 1)).isNull();
    }

    @Nested
    public class SortLateLoansListTests {

	private ArrayList<LoanBatch> loans = null;

	@BeforeEach
	public void initLoanList() {
	    reset(customerBatchDao);
	    loans = new ArrayList<>();
	    loans.add(new LoanBatch(1, LocalDate.now().minusDays(5).toString(), false, false, new LibraryBookBatch(),
		    new CustomerBatch()));
	    loans.add(new LoanBatch(2, LocalDate.now().minusDays(5).toString(), false, false, new LibraryBookBatch(),
		    new CustomerBatch()));
	    loans.add(new LoanBatch(3, LocalDate.now().toString(), false, false, new LibraryBookBatch(),
		    new CustomerBatch()));

	}

	@Test
	public void sortLateLoansListTest() {
	    when(loanBatchDao.findByReturnedFalse()).thenReturn(loans);
	    when(customerBatchDao.findById(ArgumentMatchers.any())).thenReturn(Optional.of(customer));
	    when(libraryBookBatchDao.findById(ArgumentMatchers.any())).thenReturn(Optional.of(book));
	    List<LoanBatchMailInfoDTO> list = service.sortLateLoansList();
	    assertThat(list).isNotEmpty();
	    assertThat(list.size()).isEqualTo(2);
	}

	@Test
	public void sortLateLoansListTestNullCustomer() {
	    when(loanBatchDao.findByReturnedFalse()).thenReturn(loans);
	    when(customerBatchDao.findById(ArgumentMatchers.any())).thenReturn(Optional.empty());
	    assertThatThrownBy(() -> service.sortLateLoansList()).isInstanceOf(CustomerNotFoundException.class);
	}

	@Test
	public void sortLateLoansListTestNullBook() {
	    when(loanBatchDao.findByReturnedFalse()).thenReturn(loans);
	    when(customerBatchDao.findById(ArgumentMatchers.any())).thenReturn(Optional.of(customer));
	    when(libraryBookBatchDao.findById(ArgumentMatchers.any())).thenReturn(Optional.empty());
	    assertThatThrownBy(() -> service.sortLateLoansList()).isInstanceOf(BookNotFoundException.class);
	}

    }

}

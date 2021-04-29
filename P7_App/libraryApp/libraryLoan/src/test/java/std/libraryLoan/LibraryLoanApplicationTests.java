package std.libraryLoan;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

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

import serviceExtended.LoanServiceImplForTest;
import std.libraryBookLoans.dao.CustomerLoanDAO;
import std.libraryBookLoans.dao.LibraryBookLoanDAO;
import std.libraryBookLoans.dao.LibraryBuildingLoanDAO;
import std.libraryBookLoans.dao.LibraryReservationForLoanDAO;
import std.libraryBookLoans.dao.LibraryRoleLoanDAO;
import std.libraryBookLoans.dao.LoanDAO;
import std.libraryBookLoans.dto.LoanInfoDTO;
import std.libraryBookLoans.dto.ReservableBookExamplaryDatedDTO;
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

@SpringBootTest(classes = { Loan.class, LoanServiceImplForTest.class })
@TestPropertySource(value = "/application-unitTest.properties")
@ExtendWith(MockitoExtension.class)
class LibraryLoanApplicationTests {

    @MockBean
    LoanDAO loanDAO;

    @MockBean
    CustomerLoanDAO customerDAO;

    @MockBean
    LibraryRoleLoanDAO roleDAO;

    @MockBean
    LibraryBookLoanDAO bookDAO;

    @MockBean
    LibraryBuildingLoanDAO buildingDAO;

    @MockBean
    LibraryReservationForLoanDAO reservationDAO;

    @InjectMocks
    private LoanServiceImplForTest service = new LoanServiceImplForTest();

    private Validator validator;
    private LocalDate today;
    private Loan loanTest;
    private LibraryBookLoan bookLoanTest;
    private LibraryBuildingLoan buildingLoanTest;
    private CustomerLoan customerLoanTest;
    private Optional<CustomerLoan> optCustomer;
    private LibraryRoleLoan roleLoanTest;
    private LibraryReservationForLoan reservationLoanTest;
    private List<Loan> loanListTest;

    @BeforeEach
    private void validator() {
	ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	validator = factory.getValidator();
    }

    @BeforeEach
    public void initAndSetUp() {
	reset(loanDAO);
	reset(reservationDAO);
	roleLoanTest = new LibraryRoleLoan(1, "roleType");
	buildingLoanTest = new LibraryBuildingLoan(2, "buildingTest2");
	bookLoanTest = bookLoan(1, false, "title0", 0, "buildingName0");
	customerLoanTest = customerLoan(1);
	loanTest = new Loan(1, returnDateForTest(1), false, false, bookLoanTest, customerLoanTest);
	reservationLoanTest = new LibraryReservationForLoan(1, null, false, 1, bookLoanTest, customerLoanTest);
	today = LocalDate.now();
    }

    @BeforeEach
    public void initList() {
	loanListTest = new ArrayList<Loan>();
	loanListTest.add(new Loan(2, returnDateForTest(1), false, false,
		bookLoan(2, false, "title1", 0, "buildingName1"), customerLoan(2)));
	loanListTest.add(new Loan(3, returnDateForTest(1), false, false,
		bookLoan(3, false, "title2", 0, "buildingName2"), customerLoan(2)));
	loanListTest.add(new Loan(4, returnDateForTest(1), false, true,
		bookLoan(4, false, "title3", 0, "buildingName3"), customerLoan(2)));
	loanListTest.add(new Loan(5, returnDateForTest(1), false, false,
		bookLoan(5, false, "title4", 0, "buildingName4"), customerLoan(3)));

    }

    @Test
    public void customerLoansTest() {
	customerLoanTest = customerLoan(2);
	optCustomer = Optional.of(customerLoanTest);
	System.out.println(optCustomer.get().getId());
	when(customerDAO.findOneByCustomerEmail(ArgumentMatchers.anyString())).thenReturn(optCustomer);
	when(loanDAO.findByCustomerIdAndReturnedFalse(customerLoanTest.getId())).thenReturn(loanListTest.stream()
		.filter(o -> o.getCustomer().getId().equals(customerLoanTest.getId()) && o.getReturned().equals(false))
		.collect(Collectors.toList()));
	assertThat(service.customerLoans("authUserName")).isNotEmpty();
	assertThat(service.customerLoans("authUserName").size()).isEqualTo(2);
	service.customerLoans("authUserName").forEach(o -> assertThat(o).isInstanceOf(LoanInfoDTO.class));

    }

    @Nested
    @DisplayName(value = "Loan creation ")
    public class LoanCreationTests {

	@ParameterizedTest
	@ValueSource(ints = { 0, 1, 4, 10 })
	public void postponedDateTestPass(int ints) {
	    assertThat(service.postponedDate(today.toString(), ints, ChronoUnit.DAYS))
		    .isEqualTo(today.plusDays(ints).toString());
	    assertThat(service.postponedDate(today.toString(), ints, ChronoUnit.WEEKS))
		    .isEqualTo(today.plusWeeks(ints).toString());
	    assertThat(service.postponedDate(today.toString(), ints, ChronoUnit.MONTHS))
		    .isEqualTo(today.plusMonths(ints).toString());
	    assertThat(service.postponedDate(today.toString(), ints, ChronoUnit.YEARS))
		    .isEqualTo(today.plusYears(ints).toString());
	}

	@ParameterizedTest
	@ValueSource(strings = { "HOURS", "ERAS", "HALF_DAYS" })
	public void postponedDateTestFail(String strings) {
	    ChronoUnit unit = ChronoUnit.valueOf(strings);
	    assertThatThrownBy(() -> service.postponedDate(today.toString(), 1, unit))
		    .isInstanceOf(ChronoUnitNotImplementedException.class);
	    assertThatThrownBy(() -> service.postponedDate(today.toString(), 1, null))
		    .isInstanceOf(ChronoUnitNotImplementedException.class);
	}

	@Test
	public void loanDefaultValueTestPass() {
	    Loan loan = new Loan();
	    service.loanDefaultValue(loan, 5, ChronoUnit.WEEKS);
	    assertThat(loan.getReturnDate()).isEqualTo(today.plusWeeks(5).toString());
	    assertThat(loan.getReturned()).isFalse();
	    assertThat(loan.getPostponed()).isFalse();
	}

	@Test
	public void loanDefaultValueTestPassOnNullUnitAndNbOfUNits() {
	    Loan loan = new Loan();
	    service.loanDefaultValue(loan, null, null);
	    assertThat(loan.getReturnDate()).isEqualTo(today.plusWeeks(4).toString());
	    assertThat(loan.getReturned()).isFalse();
	    assertThat(loan.getPostponed()).isFalse();
	}

	@Test
	public void loanDefaultValueTestFail() {
	    Loan loan = new Loan();
	    assertThatThrownBy(() -> service.loanDefaultValue(loan, 4, ChronoUnit.DECADES))
		    .isInstanceOf(ChronoUnitNotImplementedException.class);
	}

	@Test
	public void bookTest() {
	    when(bookDAO.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(bookLoanTest));
	    assertThat(service.book(1)).isEqualTo(bookLoanTest);
	}

	@Test
	public void bookTestFail() {
	    Integer bookId = 1;
	    when(bookDAO.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.empty());
	    assertThatThrownBy(() -> service.book(bookId)).isInstanceOf(BookNotFoundException.class)
		    .hasMessage("pas de livre sous " + bookId);
	}

	@Test
	public void loanedBookTestPass() {
	    bookLoanTest.setAvailability(true);
	    when(bookDAO.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(bookLoanTest));
	    when(loanDAO.findByBookTitleAndCustomerIdAndReturnedFalse(ArgumentMatchers.anyString(),
		    ArgumentMatchers.anyInt())).thenReturn(Optional.empty());
	    assertThat(service.loanedBook(1, 1)).isEqualTo(bookLoanTest);
	}

	@Test
	public void loanedBookTestPassFail() {
	    bookLoanTest.setAvailability(false);
	    when(bookDAO.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(bookLoanTest));
	    when(loanDAO.findByBookTitleAndCustomerIdAndReturnedFalse(ArgumentMatchers.anyString(),
		    ArgumentMatchers.anyInt())).thenReturn(Optional.of(loanTest));
	    assertThatThrownBy(() -> service.loanedBook(1, 1)).isInstanceOf(BookNotAvailableException.class);
	}

	@Test
	public void loanedBookTestPassFailNotAvailable() {
	    bookLoanTest.setAvailability(false);
	    when(bookDAO.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(bookLoanTest));
	    when(loanDAO.findByBookTitleAndCustomerIdAndReturnedFalse(ArgumentMatchers.anyString(),
		    ArgumentMatchers.anyInt())).thenReturn(Optional.empty());
	    assertThatThrownBy(() -> service.loanedBook(1, 1)).isInstanceOf(BookNotAvailableException.class);
	}

	@Test
	public void loanedBookTestPassFailTitleLoaned() {
	    bookLoanTest.setAvailability(true);
	    when(bookDAO.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(bookLoanTest));
	    when(loanDAO.findByBookTitleAndCustomerIdAndReturnedFalse(ArgumentMatchers.anyString(),
		    ArgumentMatchers.anyInt())).thenReturn(Optional.of(loanTest));
	    assertThatThrownBy(() -> service.loanedBook(1, 1)).isInstanceOf(BookNotAvailableException.class);
	}

	@Test
	public void bookBuildingTest() {
	    when(buildingDAO.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(buildingLoanTest));
	    assertThat(service.bookBuilding(1)).isEqualTo(buildingLoanTest);
	}

	@Test
	public void bookBuildingTestFail() {
	    when(buildingDAO.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.empty());
	    assertThatThrownBy(() -> service.bookBuilding(1)).isInstanceOf(BuildingNotFoundException.class);
	}

	@Test
	public void settedLoanedBookTest() {
	    bookLoanTest.setAvailability(true);
	    when(bookDAO.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(bookLoanTest));
	    when(loanDAO.findByBookTitleAndCustomerIdAndReturnedFalse(ArgumentMatchers.anyString(),
		    ArgumentMatchers.anyInt())).thenReturn(Optional.empty());
	    when(buildingDAO.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(buildingLoanTest));
	    LibraryBookLoan settedBook = service.settedLoanedBook(1, 1);
	    assertThat(settedBook.getAvailability()).isFalse();
	    assertThat(settedBook.getLibraryBuilding().equals(buildingLoanTest)).isTrue();
	}

	@Test
	public void settedLoanedBookTestFail() {
	    bookLoanTest.setAvailability(false);
	    when(bookDAO.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(bookLoanTest));
	    when(loanDAO.findByBookTitleAndCustomerIdAndReturnedFalse(ArgumentMatchers.anyString(),
		    ArgumentMatchers.anyInt())).thenReturn(Optional.of(loanTest));
	    when(buildingDAO.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(buildingLoanTest));
	    assertThatThrownBy(() -> service.settedLoanedBook(1, 1).getAvailability())
		    .isInstanceOf(BookNotAvailableException.class);
	}

	@Test
	public void customerLoanTest() {
	    when(customerDAO.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(customerLoanTest));
	    assertThat(service.customerLoan(1)).isEqualTo(customerLoanTest);
	}

	@Test
	public void customerLoanTestFail() {
	    when(customerDAO.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.empty());
	    assertThatThrownBy(() -> service.customerLoan(1)).isInstanceOf(CustomerNotFoundException.class);
	}

	@Test
	public void roleLoanTest() {
	    when(roleDAO.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(roleLoanTest));
	    assertThat(service.roleLoan(1)).isEqualTo(roleLoanTest);
	}

	@Test
	public void roleLoanTestFail() {
	    when(roleDAO.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.empty());
	    assertThatThrownBy(() -> service.roleLoan(1)).isInstanceOf(RoleNotFoundException.class);
	}

	@Test
	public void settedCustomerLoanTest() {
	    when(customerDAO.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(customerLoanTest));
	    when(roleDAO.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(roleLoanTest));
	    assertThatCode(() -> service.settedCustomerLoan(1)).doesNotThrowAnyException();
	}

	@Test
	public void settedCustomerLoanTestFailCustomer() {
	    when(customerDAO.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.empty());
	    when(roleDAO.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(roleLoanTest));
	    assertThatThrownBy(() -> service.settedCustomerLoan(1)).isInstanceOf(CustomerNotFoundException.class);
	}

	@Test
	public void settedCustomerLoanTestFailRole() {
	    when(customerDAO.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(customerLoanTest));
	    when(roleDAO.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.empty());
	    assertThatThrownBy(() -> service.settedCustomerLoan(1)).isInstanceOf(RoleNotFoundException.class);
	}

	@Test
	public void createLoanUsingBookIdTest() {
	    bookLoanTest.setAvailability(true);
	    when(bookDAO.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(bookLoanTest));
	    when(loanDAO.findByBookTitleAndCustomerIdAndReturnedFalse(ArgumentMatchers.anyString(),
		    ArgumentMatchers.anyInt())).thenReturn(Optional.empty());
	    when(buildingDAO.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(buildingLoanTest));
	    when(customerDAO.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(customerLoanTest));
	    when(roleDAO.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(roleLoanTest));
	    Loan loan = service.createLoanUsingBookId(1, 1, 5, "weeks");
	    assertThat(constrainCheck(loan)).isTrue();
	    assertThat(loan.getReturnDate()).isEqualTo(today.plusWeeks(5).toString());
	}

	@Test
	public void createLoanTest() {
	    bookLoanTest.setAvailability(true);
	    when(bookDAO.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(bookLoanTest));
	    when(loanDAO.findByBookTitleAndCustomerIdAndReturnedFalse(ArgumentMatchers.anyString(),
		    ArgumentMatchers.anyInt())).thenReturn(Optional.empty());
	    when(buildingDAO.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(buildingLoanTest));
	    when(customerDAO.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(customerLoanTest));
	    when(roleDAO.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(roleLoanTest));
	    when(reservationDAO.findByBookIdAndCustomerIdAndCanceledStatusFalse(ArgumentMatchers.anyInt(),
		    ArgumentMatchers.anyInt())).thenReturn(Optional.empty());
	    assertThatCode(() -> service.createLoan(1, 1, 1, "weeks")).doesNotThrowAnyException();
	    verify(loanDAO, times(1)).saveAndFlush(ArgumentMatchers.any(Loan.class));
	}

	@Test
	public void createLoanTestFail() {
	    when(reservationDAO.findByBookIdAndCustomerIdAndCanceledStatusFalse(ArgumentMatchers.anyInt(),
		    ArgumentMatchers.anyInt())).thenReturn(Optional.of(reservationLoanTest));
	    assertThatThrownBy(() -> service.createLoan(1, 1, 1, "weeks")).isInstanceOf(BookNotAvailableException.class)
		    .hasMessage("Loan service : book reserved or customer alreadyloaned it");
	    verify(loanDAO, times(0)).saveAndFlush(ArgumentMatchers.any(Loan.class));
	}

	@Test
	public void reservationTest() {
	    when(reservationDAO.findByIdAndCanceledStatusFalse(ArgumentMatchers.anyInt()))
		    .thenReturn(Optional.of(reservationLoanTest));
	    assertThatCode(() -> service.reservation(1)).doesNotThrowAnyException();
	}

	@Test
	public void reservationTestFail() {
	    when(reservationDAO.findByIdAndCanceledStatusFalse(ArgumentMatchers.anyInt())).thenReturn(Optional.empty());
	    assertThatThrownBy(() -> service.reservation(1)).isInstanceOf(ReservationNotFoundException.class)
		    .hasMessage("Reservation ref = [1] not found.");
	}

	@Test
	public void isRigthCustomerTest() {
	    assertThat(service.isRigthCustomer(1, reservationLoanTest)).isTrue();
	    assertThat(service.isRigthCustomer(2, reservationLoanTest)).isFalse();
	}

	@Test
	public void setBookNumberOfreservationTest() {
	    bookLoanTest.setNumberOfReservations(2);
	    service.setBookNumberOfreservation(bookLoanTest);
	    assertThat(bookLoanTest.getNumberOfReservations()).isEqualTo(1);
	    bookLoanTest.setNumberOfReservations(0);
	    service.setBookNumberOfreservation(bookLoanTest);
	    assertThat(bookLoanTest.getNumberOfReservations()).isEqualTo(0);
	}

	@Test
	public void updateBookFromReservationTest() {
	    bookLoanTest.setNumberOfReservations(1);
	    LibraryBookLoan book = service.updateBookFromReservation(bookLoanTest);
	    assertThat(book.getAvailability()).isFalse();
	    assertThat(book.getNumberOfReservations()).isEqualTo(0);
	    assertThat(service.updateBookFromReservation(bookLoanTest).getNumberOfReservations()).isEqualTo(0);

	}

	@Test
	public void createLoanUsingBookTest() {
	    bookLoanTest.setAvailability(true);
	    bookLoanTest.setNumberOfReservations(2);
	    ;
	    when(customerDAO.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(customerLoanTest));
	    when(roleDAO.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(roleLoanTest));
	    Loan loan = service.createLoanUsingBook(1, bookLoanTest, 5, "weeks");
	    assertThat(loan.getBook().getNumberOfReservations()).isEqualTo(1);
	    assertThat(loan.getBook().getAvailability()).isFalse();
	    assertThat(loan.getCustomer()).isEqualTo(customerLoanTest);
	    assertThat(loan.getReturnDate()).isEqualTo(today.plusWeeks(5).toString());
	}

	@Test
	public void updateReservationOnLoanTest() {
	    LibraryReservationForLoan reservation = service.updateReservationOnLoan(reservationLoanTest);
	    assertThat(reservation.getCanceledStatus()).isTrue();
	    assertThat(reservation.getPriority()).isEqualTo(-1);
	}

	@Test
	public void updatePriorityTest() {
	    LibraryReservationForLoan reservation = service.updatePriority(reservationLoanTest);
	    assertThat(reservation.getPriority()).isEqualTo(0);
	    assertThat(service.updatePriority(reservationLoanTest).getPriority()).isEqualTo(0);
	}

	@Test
	public void reservationUpdatedPriorityListTest() {
	    List<LibraryReservationForLoan> rawList = listReservationTest();
	    when(reservationDAO.findByBookIdAndCanceledStatusFalse(6)).thenReturn(
		    rawList.stream().filter(o -> o.getBook().getId().equals(6)).collect(Collectors.toList()));
	    when(reservationDAO.findByBookIdAndCanceledStatusFalse(7))
		    .thenReturn(new ArrayList<LibraryReservationForLoan>());
	    List<LibraryReservationForLoan> list = service.reservationUpdatedPriorityList(6);
	    assertThat(list.size()).isEqualTo(2);
	    list.forEach(o -> assertThat(o.getPriority()).isEqualTo(0));
	    assertThat(service.reservationUpdatedPriorityList(7)).isEmpty();
	}

	@Test
	public void updateReservationsPriorityTest() {
	    when(reservationDAO.findByBookIdAndCanceledStatusFalse(6)).thenReturn(listReservationTest());
	    service.updateReservationsPriority(6);
	    verify(reservationDAO, times(1)).saveAll(ArgumentMatchers.anyIterable());

	}

	@Test
	public void updateReservationsPriorityTestEmpty() {
	    when(reservationDAO.findByBookIdAndCanceledStatusFalse(6))
		    .thenReturn(new ArrayList<LibraryReservationForLoan>());
	    service.updateReservationsPriority(6);
	    verify(reservationDAO, times(0)).saveAll(ArgumentMatchers.anyIterable());

	}

	private List<LibraryReservationForLoan> listReservationTest() {
	    List<LibraryReservationForLoan> list = new ArrayList<LibraryReservationForLoan>();
	    list.add(new LibraryReservationForLoan(2, null, false, 1, bookLoan(6, true, "title6", 1, "buildingName"),
		    customerLoan(2)));
	    list.add(new LibraryReservationForLoan(3, null, false, 1, bookLoan(6, true, "title6", 1, "buildingName"),
		    customerLoan(3)));
	    list.add(new LibraryReservationForLoan(4, null, false, 1, bookLoan(7, true, "title7", 1, "buildingName"),
		    customerLoan(2)));
	    return list;
	}

	@Test
	public void createLoanFromReservationTest() {
	    when(reservationDAO.findByIdAndCanceledStatusFalse(ArgumentMatchers.anyInt()))
		    .thenReturn(Optional.of(reservationLoanTest));
	    when(customerDAO.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(customerLoanTest));
	    when(roleDAO.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(roleLoanTest));
	    assertThatCode(() -> service.createLoanFromReservation(1, 1, 5, "weeks")).doesNotThrowAnyException();
	}

	@Test
	public void createLoanFromReservationTestFail() {
	    customerLoanTest.setId(2);
	    when(reservationDAO.findByIdAndCanceledStatusFalse(ArgumentMatchers.anyInt()))
		    .thenReturn(Optional.of(reservationLoanTest));
	    when(customerDAO.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(customerLoanTest));
	    when(roleDAO.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(roleLoanTest));
	    assertThatThrownBy(() -> service.createLoanFromReservation(1, 1, 5, "weeks"))
		    .isInstanceOf(BookNotAvailableException.class)
		    .hasMessage("Loan service: customer not corresponding to the one on reservation ");
	}

    }

    @Test
    public void returnLoanTest() {
	when(loanDAO.findByBookIdAndCustomerIdAndReturnedFalse(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt()))
		.thenReturn(Optional.of(loanTest));
	assertThatCode(() -> service.returnLoan(1, 1)).doesNotThrowAnyException();
	assertThat(loanTest.getReturned()).isTrue();
	assertThat(bookLoanTest.getAvailability()).isTrue();
	verify(loanDAO, times(1)).saveAndFlush(ArgumentMatchers.any(Loan.class));
    }

    @Test
    public void returnLoanTestFail() {
	when(loanDAO.findByBookIdAndCustomerIdAndReturnedFalse(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt()))
		.thenReturn(Optional.empty());
	assertThatThrownBy(() -> service.returnLoan(1, 1)).isInstanceOf(LoanUnknownException.class)
		.hasMessage("Erreur aucune correspondance");
	assertThat(loanTest.getReturned()).isFalse();
	assertThat(bookLoanTest.getAvailability()).isFalse();
	verify(loanDAO, times(0)).saveAndFlush(ArgumentMatchers.any(Loan.class));
    }

    @Test
    public void postponeLoanTest() {
	loanTest.setReturnDate(today.toString());
	when(loanDAO.findByIdAndCustomerCustomerEmail(ArgumentMatchers.anyInt(), ArgumentMatchers.anyString()))
		.thenReturn(Optional.of(loanTest));
	assertThatCode(() -> service.postponeLoan(1, "userName", 5, "weeks", null, null)).doesNotThrowAnyException();
	assertThat(loanTest.getReturnDate()).isEqualTo(today.plusWeeks(5).toString());
	assertThat(loanTest.getPostponed()).isTrue();
	verify(loanDAO, times(1)).saveAndFlush(ArgumentMatchers.any(Loan.class));

    }

    @Test
    public void postponeLoanTestFail() {
	loanTest.setReturnDate(today.toString());
	when(loanDAO.findByIdAndCustomerCustomerEmail(ArgumentMatchers.anyInt(), ArgumentMatchers.anyString()))
		.thenReturn(Optional.empty());
	assertThatThrownBy(() -> service.postponeLoan(1, "userName", 5, "weeks", null, null))
		.isInstanceOf(LoanNotFoundException.class);
	assertThat(loanTest.getReturnDate()).isEqualTo(today.toString());
	assertThat(loanTest.getPostponed()).isFalse();
	verify(loanDAO, times(0)).saveAndFlush(ArgumentMatchers.any(Loan.class));

    }

    @Test
    public void postponeLoanTestDaysOff() {
	loanTest.setReturnDate(today.toString());
	ArrayList<DayOfWeek> daysOfWeeks = new ArrayList<>();
	daysOfWeeks.add(today.getDayOfWeek());
	when(loanDAO.findByIdAndCustomerCustomerEmail(ArgumentMatchers.anyInt(), ArgumentMatchers.anyString()))
		.thenReturn(Optional.of(loanTest));
	assertThatCode(() -> service.postponeLoan(1, "userName", 5, "weeks", daysOfWeeks, null))
		.doesNotThrowAnyException();
	assertThat(LocalDate.parse(loanTest.getReturnDate()).getDayOfWeek())
		.isEqualTo(today.plusDays(1).getDayOfWeek());
	assertThat(loanTest.getPostponed()).isTrue();
	verify(loanDAO, times(1)).saveAndFlush(ArgumentMatchers.any(Loan.class));
    }

    @Test
    public void postponeLoanTestHollidays() {
	Integer postponeUnitNumber = 5;
	loanTest.setReturnDate(today.toString());
	ArrayList<LocalDate> holidays = new ArrayList<LocalDate>();
	holidays.add(today.plusWeeks(postponeUnitNumber));
	when(loanDAO.findByIdAndCustomerCustomerEmail(ArgumentMatchers.anyInt(), ArgumentMatchers.anyString()))
		.thenReturn(Optional.of(loanTest));
	assertThatCode(() -> service.postponeLoan(1, "userName", 5, "weeks", null, holidays))
		.doesNotThrowAnyException();
	assertThat(loanTest.getReturnDate()).isEqualTo(today.plusWeeks(postponeUnitNumber).plusDays(1).toString());
	assertThat(loanTest.getPostponed()).isTrue();
	verify(loanDAO, times(1)).saveAndFlush(ArgumentMatchers.any(Loan.class));
    }

    @Test
    public void reservableBookExamplaryDTOsTest() {
	when(loanDAO.findByReturnedFalseAndBookIdIn(ArgumentMatchers.anyList())).thenReturn(loanListTest);
	List<ReservableBookExamplaryDatedDTO> list = service.reservableBookExamplaryDTOs(new ArrayList<Integer>(), 5,
		"weeks");
	list.forEach(o -> assertThat(o).isInstanceOf(ReservableBookExamplaryDatedDTO.class));
	list.forEach(o -> assertThat(o.getClosestReturnDate()).isEqualTo(returnDateForTest(1)));
	list.forEach(o -> assertThat(o.getFarrestReturnDate()).isEqualTo(LocalDate.now().plusWeeks(6).toString()));
    }

    @Test
    public void reservableBookExamplaryDTOsTestPostPonedLoan() {
	loanListTest.forEach(o -> o.setPostponed(true));
	when(loanDAO.findByReturnedFalseAndBookIdIn(ArgumentMatchers.anyList())).thenReturn(loanListTest);
	List<ReservableBookExamplaryDatedDTO> list = service.reservableBookExamplaryDTOs(new ArrayList<Integer>(), 5,
		"weeks");

	list.forEach(o -> assertThat(o.getFarrestReturnDate()).isEqualTo("none"));
    }

    @Nested
    @DisplayName(value = "Loan constraints check")
    public class loanConstraintsCheckTests {

	@Test
	public void loanTestPass() {
	    assertThat(constrainCheck(loanTest)).isTrue();
	}

	@Test
	public void loanTestFail() {
	    loanTest = new Loan();
	    assertThat(constrainCheck(loanTest)).isFalse();
	}

	@Test
	public void loanTestFailReturnDate() {
	    loanTest.setReturnDate(null);
	    assertThat(constrainCheck(loanTest)).isFalse();
	}

	@Test
	public void loanTestFailPostPone() {
	    loanTest.setPostponed(null);
	    assertThat(constrainCheck(loanTest)).isFalse();
	}

	@Test
	public void loanTestFailReturned() {
	    loanTest.setReturned(null);
	    assertThat(constrainCheck(loanTest)).isFalse();
	}

	@Test
	public void loanTestFailBook() {
	    loanTest.setBook(null);
	    ;
	    assertThat(constrainCheck(loanTest)).isFalse();
	}

	@Test
	public void loanTestFailCustomer() {
	    loanTest.setCustomer(null);
	    assertThat(constrainCheck(loanTest)).isFalse();
	}

    }

    private LibraryBookLoan bookLoan(Integer id, Boolean availability, String title, Integer nbReservation,
	    String buildingName) {
	LibraryBookLoan book = new LibraryBookLoan();
	book.setId(id);
	book.setAvailability(availability);
	book.setLibraryBuilding(new LibraryBuildingLoan(1, buildingName));
	book.setNumberOfReservations(nbReservation);
	book.setTitle(title);
	return book;
    }

    private CustomerLoan customerLoan(Integer id) {
	CustomerLoan customer = new CustomerLoan();
	customer.setId(id);
	customer.setRole(new LibraryRoleLoan(1, "admin"));
	return customer;
    }

    private Boolean constrainCheck(Loan loan) {
	Set<ConstraintViolation<Loan>> validation = validator.validate(loan);
	return validation.isEmpty();

    }

    private String returnDateForTest(Integer numberOfWeeksToAddToNow) {
	return LocalDate.now().plusWeeks(numberOfWeeksToAddToNow).toString();
    }

}

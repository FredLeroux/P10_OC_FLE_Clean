package std.libraryUi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.servlet.ModelAndView;

import std.libraryUi.beans.BooksBean;
import std.libraryUi.beans.LibraryBookLoanInfoBean;
import std.libraryUi.beans.LibraryCustomerBean;
import std.libraryUi.beans.LibraryReservationBean;
import std.libraryUi.beans.LoanInfoBean;
import std.libraryUi.beans.ReservableBookExamplaryBean;
import std.libraryUi.beans.ReservableBookExamplaryDatedBean;
import std.libraryUi.controller.controllerMethods.ControllerMethods;
import std.libraryUi.dto.UiNotificationReservationDTO;
import std.libraryUi.dto.UiReservationDTO;
import std.libraryUi.proxies.LibraryBookCaseProxy;
import std.libraryUi.proxies.LibraryBookLoansProxy;
import std.libraryUi.proxies.LibraryCustomerProxy;
import std.libraryUi.proxies.LibraryReservationsProxy;
import std.libraryUi.proxies.LibraryScheduledBatchAndMailingProxy;

@SpringBootTest(classes = { ControllerMethods.class })
@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "/application-unitTest.properties")
class LibraryUiApplicationTests {

    @MockBean
    private LibraryBookLoansProxy libraryBookLoansProxy;

    @MockBean
    private LibraryCustomerProxy libraryCustomerProxy;

    @MockBean
    private LibraryBookCaseProxy libraryBookCaseProxy;

    @MockBean
    private LibraryReservationsProxy libraryReservationsProxy;

    @MockBean
    private LibraryScheduledBatchAndMailingProxy libraryScheduledBatchAndMailingProxy;

    @MockBean
    private HttpServletRequest request;

    @InjectMocks
    private ControllerMethods methods = new ControllerMethods();

    private List<LoanInfoBean> loans;
    private List<ReservableBookExamplaryBean> reservableBooks;
    private List<ReservableBookExamplaryDatedBean> reservableBooksDateds;
    private List<LibraryReservationBean> reservations;

    @BeforeEach
    public void init() {
	LocalDate date = LocalDate.now();
	loans = new ArrayList<LoanInfoBean>();
	loans.add(new LoanInfoBean(1, LocalDate.now().toString(), false, new LibraryBookLoanInfoBean("title1")));
	loans.add(new LoanInfoBean(2, LocalDate.now().toString(), false, new LibraryBookLoanInfoBean("title2")));
	loans.add(new LoanInfoBean(3, LocalDate.now().toString(), false, new LibraryBookLoanInfoBean("title3")));
	reservableBooks = new ArrayList<ReservableBookExamplaryBean>();
	reservableBooks.add(new ReservableBookExamplaryBean(1));
	reservableBooks.add(new ReservableBookExamplaryBean(2));
	reservableBooks.add(new ReservableBookExamplaryBean(3));
	reservableBooksDateds = new ArrayList<ReservableBookExamplaryDatedBean>();
	reservableBooksDateds.add(new ReservableBookExamplaryDatedBean(1, LocalDate.now().toString(),
		LocalDate.now().plusWeeks(4).toString()));
	reservableBooksDateds.add(new ReservableBookExamplaryDatedBean(2, LocalDate.now().toString(),
		LocalDate.now().plusWeeks(4).toString()));
	reservableBooksDateds.add(new ReservableBookExamplaryDatedBean(3, LocalDate.now().toString(),
		LocalDate.now().plusWeeks(4).toString()));
	reservations = new ArrayList<LibraryReservationBean>();
	reservations.add(new LibraryReservationBean(1, date.toString(), false, 1, "BuildingName", date.toString(),
		false, new BooksBean(1, "kind1", "title1", "author1", "libraryBuildingName1", 2),
		new LibraryCustomerBean()));
	reservations.add(new LibraryReservationBean(2, date.toString(), false, 1, "BuildingName", "available", false,
		new BooksBean(2, "kind2", "title2", "author2", "libraryBuildingName2", 1), new LibraryCustomerBean()));
	reservations.add(new LibraryReservationBean(3, null, false, 3, "BuildingName", date.toString(), false,
		new BooksBean(3, "kind3", "title3", "author3", "libraryBuildingName3", 0), new LibraryCustomerBean()));

    }

    @Test
    public void addSortedLoansListAndDatePickerLoansInfoToModelTest() {
	when(request.getHeader(ArgumentMatchers.any())).thenReturn("true");
	when(libraryCustomerProxy.getCustomerUserName(ArgumentMatchers.any())).thenReturn("customer");
	when(libraryBookLoansProxy.loansList(ArgumentMatchers.any())).thenReturn(loans);
	ModelAndView model = new ModelAndView();
	methods.addSortedLoansListAndDatePickerLoansInfoToModel(model, "listModelAttrName",
		"datepickerInfoModelAttrName", request, "headerName", "fr");
	assertThat(model.getModelMap().get("listModelAttrName")).isNotNull();
	assertThat(model.getModelMap().get("datepickerInfoModelAttrName")).isNotNull();
    }

    @Test
    public void addSortedLoansListAndDatePickerLoansInfoToModelTestHeaderFalse() {
	when(request.getHeader(ArgumentMatchers.any())).thenReturn(null);
	ModelAndView model = new ModelAndView();
	methods.addSortedLoansListAndDatePickerLoansInfoToModel(model, "listModelAttrName",
		"datepickerInfoModelAttrName", request, "headerName", "fr");
	assertThat(model.getModelMap().get("listModelAttrName")).isNull();
	assertThat(model.getModelMap().get("datepickerInfoModelAttrName")).isNull();
    }

    @Test
    public void addSortedLoansListAndDatePickerLoansInfoToModelTestUserNameNull() {
	when(request.getHeader(ArgumentMatchers.any())).thenReturn("true");
	when(libraryCustomerProxy.getCustomerUserName(ArgumentMatchers.any())).thenReturn(null);
	ModelAndView model = new ModelAndView();
	methods.addSortedLoansListAndDatePickerLoansInfoToModel(model, "listModelAttrName",
		"datepickerInfoModelAttrName", request, "headerName", "fr");
	assertThat(model.getModelMap().get("listModelAttrName")).isNull();
	assertThat(model.getModelMap().get("datepickerInfoModelAttrName")).isNull();
    }

    @Test
    public void postPoneLoanTest() {
	when(request.getHeader(ArgumentMatchers.any())).thenReturn("true");
	when(libraryCustomerProxy.getCustomerUserName(ArgumentMatchers.any())).thenReturn("customer");
	methods.postPoneLoan(request, "headerName", 1, 1, ChronoUnit.DAYS);
	verify(libraryBookLoansProxy, times(1)).postpone(ArgumentMatchers.any(), ArgumentMatchers.any(),
		ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    public void postPoneLoanTestNullHeader() {
	when(request.getHeader(ArgumentMatchers.any())).thenReturn(null);
	methods.postPoneLoan(request, "headerName", 1, 1, ChronoUnit.DAYS);
	verify(libraryBookLoansProxy, times(0)).postpone(ArgumentMatchers.any(), ArgumentMatchers.any(),
		ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    public void proxyCall() {
	methods.createLoan(1, 1);
	methods.createLoanFromReservation(1, 1);
	methods.cancelReservation(1, 1);
	verify(libraryBookLoansProxy, times(1)).createLoan(1, 1, 4, "weeks");
	verify(libraryBookLoansProxy, times(1)).createLoanFromReservation(1, 1, 4, "weeks");
	verify(libraryReservationsProxy, times(1)).cancelReservation(1);
	verify(libraryScheduledBatchAndMailingProxy, times(1)).sendNotificationBookAvailableAfterCustomerCancel(1, 1);

    }

    @Test
    public void postPoneLoanTestNullUserName() {
	when(request.getHeader(ArgumentMatchers.any())).thenReturn("true");
	when(libraryCustomerProxy.getCustomerUserName(ArgumentMatchers.any())).thenReturn(null);
	methods.postPoneLoan(request, "headerName", 1, 1, ChronoUnit.DAYS);
	verify(libraryBookLoansProxy, times(0)).postpone(ArgumentMatchers.any(), ArgumentMatchers.any(),
		ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    public void returnLoanTest() {
	when(libraryReservationsProxy.customerToNotified(ArgumentMatchers.any(), ArgumentMatchers.any()))
		.thenReturn(new UiNotificationReservationDTO("customerEmail", "bookTitle", "building", 1));
	methods.returnLoan(1, 1);
	verify(libraryBookLoansProxy, times(1)).returnLoan(ArgumentMatchers.any(), ArgumentMatchers.any());
	verify(libraryScheduledBatchAndMailingProxy, times(1)).sendNotificationBookAvailable(ArgumentMatchers.any(),
		ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    public void returnLoanTestNullNotification() {
	when(libraryReservationsProxy.customerToNotified(ArgumentMatchers.any(), ArgumentMatchers.any()))
		.thenReturn(null);
	methods.returnLoan(1, 1);
	verify(libraryBookLoansProxy, times(1)).returnLoan(ArgumentMatchers.any(), ArgumentMatchers.any());
	verify(libraryScheduledBatchAndMailingProxy, times(0)).sendNotificationBookAvailable(ArgumentMatchers.any(),
		ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    public void getReservableExamplaryBeansTest() {
	when(libraryBookCaseProxy.getReservableBooks(ArgumentMatchers.any(), ArgumentMatchers.any(),
		ArgumentMatchers.any())).thenReturn(reservableBooks);
	when(libraryBookLoansProxy.reservableBookExamplaryDTOs(ArgumentMatchers.anyList(), ArgumentMatchers.any(),
		ArgumentMatchers.any())).thenReturn(reservableBooksDateds);
	List<ReservableBookExamplaryDatedBean> list = methods.getReservableExamplaryBeans("title", "buildingName", 2, 4,
		"weeks", "fr");
	assertThat(list).isNotEmpty();
	assertThat(list.size()).isEqualTo(reservableBooksDateds.size());
	list.forEach(o -> assertThat(o.getClosestReturnDate()).isEqualTo(LocalDate.now()
		.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(new Locale("fr"))).toString()));
	list.forEach(o -> assertThat(o.getFarrestReturnDate()).isEqualTo(LocalDate.now().plusWeeks(4)
		.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(new Locale("fr"))).toString()));
    }

    @Test
    public void getReservableExamplaryBeansTestFarrestNone() {
	reservableBooksDateds.forEach(o -> o.setFarrestReturnDate("none"));
	when(libraryBookCaseProxy.getReservableBooks(ArgumentMatchers.any(), ArgumentMatchers.any(),
		ArgumentMatchers.any())).thenReturn(reservableBooks);
	when(libraryBookLoansProxy.reservableBookExamplaryDTOs(ArgumentMatchers.anyList(), ArgumentMatchers.any(),
		ArgumentMatchers.any())).thenReturn(reservableBooksDateds);
	List<ReservableBookExamplaryDatedBean> list = methods.getReservableExamplaryBeans("title", "buildingName", 2, 4,
		"weeks", "fr");
	list.forEach(o -> assertThat(o.getFarrestReturnDate()).isEqualTo("none"));
    }

    @Test
    public void createReservationTest() {
	when(request.getHeader(ArgumentMatchers.any())).thenReturn("true");
	when(libraryCustomerProxy.getCustomerUserName(ArgumentMatchers.any())).thenReturn("customer");
	methods.createReservation(request, "headerName", 1);
	verify(libraryReservationsProxy, times(1)).createReservation(ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    public void createReservationTestHeaderNull() {
	when(request.getHeader(ArgumentMatchers.any())).thenReturn(null);
	methods.createReservation(request, "headerName", 1);
	verify(libraryReservationsProxy, times(0)).createReservation(ArgumentMatchers.any(), ArgumentMatchers.any());
    }

    @Test
    public void createReservationTestUserNameNull() {
	when(request.getHeader(ArgumentMatchers.any())).thenReturn("true");
	when(libraryCustomerProxy.getCustomerUserName(ArgumentMatchers.any())).thenReturn(null);
	methods.createReservation(request, "headerName", 1);
	verify(libraryReservationsProxy, times(0)).createReservation(ArgumentMatchers.any(), ArgumentMatchers.any());

    }

    @SuppressWarnings("unchecked")
    @Test
    public void customerReservationsTest() {
	when(request.getHeader(ArgumentMatchers.any())).thenReturn("true");
	when(libraryCustomerProxy.getCustomerUserName(ArgumentMatchers.any())).thenReturn("customer");
	when(libraryReservationsProxy.customerReservations(ArgumentMatchers.any())).thenReturn(reservations);
	ModelAndView model = new ModelAndView();
	methods.customerReservations(model, request, "headerName");
	List<UiReservationDTO> list = (List<UiReservationDTO>) model.getModelMap().get("reservationsList");
	assertThat(list.size()).isEqualTo(reservations.size());
	assertThat(list.stream().filter(o -> o.getReturnDate().equals("available")).collect(Collectors.toList()).size())
		.isEqualTo(1);
	assertThat(list.stream().filter(o -> o.getNotificationDate() == null).collect(Collectors.toList()).size())
		.isEqualTo(1);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void customerReservationsTestReservationEmpty() {
	reservations.clear();
	when(request.getHeader(ArgumentMatchers.any())).thenReturn("true");
	when(libraryCustomerProxy.getCustomerUserName(ArgumentMatchers.any())).thenReturn("customer");
	when(libraryReservationsProxy.customerReservations(ArgumentMatchers.any())).thenReturn(reservations);
	ModelAndView model = new ModelAndView();
	methods.customerReservations(model, request, "headerName");
	List<UiReservationDTO> list = (List<UiReservationDTO>) model.getModelMap().get("reservationsList");
	assertThat(list).isNull();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void customerReservationsTestHeaderNull() {
	reservations.clear();
	when(request.getHeader(ArgumentMatchers.any())).thenReturn(null);
	ModelAndView model = new ModelAndView();
	methods.customerReservations(model, request, "headerName");
	List<UiReservationDTO> list = (List<UiReservationDTO>) model.getModelMap().get("reservationsList");
	assertThat(list).isNull();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void customerReservationsTestUserNameEmpty() {
	reservations.clear();
	when(request.getHeader(ArgumentMatchers.any())).thenReturn("true");
	when(libraryCustomerProxy.getCustomerUserName(ArgumentMatchers.any())).thenReturn(null);
	ModelAndView model = new ModelAndView();
	methods.customerReservations(model, request, "headerName");
	List<UiReservationDTO> list = (List<UiReservationDTO>) model.getModelMap().get("reservationsList");
	assertThat(list).isNull();
    }

}

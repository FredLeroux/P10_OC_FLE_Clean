package std.libraryUi.controller.controllerMethods;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import std.libraryUi.beans.LibraryReservationBean;
import std.libraryUi.beans.LoanInfoBean;
import std.libraryUi.beans.ReservableBookExamplaryBean;
import std.libraryUi.beans.ReservableBookExamplaryDatedBean;
import std.libraryUi.beans.ReservableBookLinkedLoanBean;
import std.libraryUi.dto.UiLoanDateAndBooksList;
import std.libraryUi.dto.UiLoanInfoDTO;
import std.libraryUi.dto.UiNotificationReservationDTO;
import std.libraryUi.dto.UiReservationDTO;
import std.libraryUi.proxies.LibraryBookCaseProxy;
import std.libraryUi.proxies.LibraryBookLoansProxy;
import std.libraryUi.proxies.LibraryCustomerProxy;
import std.libraryUi.proxies.LibraryReservationsProxy;
import std.libraryUi.proxies.LibraryScheduledBatchAndMailingProxy;

@Service
public class ControllerMethods {

    @Autowired
    private LibraryBookLoansProxy libraryBookLoansProxy;

    @Autowired
    private LibraryCustomerProxy libraryCustomerProxy;

    @Autowired
    private LibraryBookCaseProxy libraryBookCaseProxy;

    @Autowired
    private LibraryReservationsProxy libraryReservationsProxy;

    @Autowired
    private LibraryScheduledBatchAndMailingProxy libraryScheduledBatchAndMailingProxy;

    /**
     * the minimum integer value to be prioritized
     */
    private static final Integer PRIORITY_VALUE = 1;

    public void addSortedLoansListAndDatePickerLoansInfoToModel(ModelAndView model, String listModelAttrName,
	    String datepickerInfoModelAttrName, HttpServletRequest request, String headerName, String language) {
	if (isHeaderPresent(request, headerName)) {
	    String userName = userNameByToken(token(request, headerName));
	    if (userName != null) {
		List<LoanInfoBean> list = loansList(userName);
		model.addObject(listModelAttrName, loanInfoDTOList(list, language));
		model.addObject(datepickerInfoModelAttrName, loanInfoToDatepicker(list));
	    }
	}
    }

    public void postPoneLoan(HttpServletRequest request, String headerName, Integer loanId, Integer unitNumber,
	    ChronoUnit unit) {
	if (isHeaderPresent(request, headerName)) {
	    String userName = userNameByToken(token(request, headerName));
	    if (userName != null) {
		libraryBookLoansProxy.postpone(loanId, userName, unitNumber, unit);
	    }
	}
    }

    public void createLoan(Integer customerId, Integer bookId) {
	libraryBookLoansProxy.createLoan(customerId, bookId, 4, "weeks");
    }

    public void createLoanFromReservation(Integer customerId, Integer reservationId) {
	libraryBookLoansProxy.createLoanFromReservation(reservationId, customerId, 4, "weeks");
    }

    public void returnLoan(Integer customerId, Integer bookId) {
	libraryBookLoansProxy.returnLoan(customerId, bookId);
	sendNotification(bookId);
    }

    private void sendNotification(Integer bookId) {
	UiNotificationReservationDTO dto = libraryReservationsProxy.customerToNotified(bookId, PRIORITY_VALUE);
	if (dto != null) {
	    libraryScheduledBatchAndMailingProxy.sendNotificationBookAvailable(dto.getCustomerEmail(),
		    dto.getBookTitle(), dto.getBuilding(), dto.getReference());
	}
    }

    private List<LoanInfoBean> loansList(String userName) {
	return libraryBookLoansProxy.loansList(userName);
    }

    private String token(HttpServletRequest request, String headerName) {
	return request.getHeader(headerName);
    }

    private List<UiLoanInfoDTO> loanInfoDTOList(List<LoanInfoBean> list, String language) {
	dateSortedList(list);
	return list.stream().map(O -> loanInfoDTOMapper(O, language)).collect(Collectors.toList());
    }

    private void dateSortedList(List<LoanInfoBean> list) {
	list.sort((LoanInfoBean O1, LoanInfoBean O2) -> parseStringToDate(O1.getReturnDate())
		.compareTo(parseStringToDate(O2.getReturnDate())));
    }

    public List<UiLoanDateAndBooksList> loanInfoToDatepicker(List<LoanInfoBean> list) {
	LinkedHashMap<String, UiLoanDateAndBooksList> lH = new LinkedHashMap<>();
	list.forEach(O -> dateWithAssociatedBooks(lH, O));
	return new ArrayList<>(lH.values());
    }

    private void dateWithAssociatedBooks(LinkedHashMap<String, UiLoanDateAndBooksList> lH, LoanInfoBean loanInfoBean) {
	String key = loanInfoBean.getReturnDate();
	if (lH.containsKey(key)) {
	    String content = lH.get(key).getBooksTitle().concat("\n" + loanInfoBean.getBook().getTitle());
	    lH.get(key).setBooksTitle(content);

	} else {
	    lH.put(key, uiLoanDateAndBooksList(loanInfoBean));
	}

    }

    private UiLoanDateAndBooksList uiLoanDateAndBooksList(LoanInfoBean loanInfoBean) {
	LocalDate date = LocalDate.parse(loanInfoBean.getReturnDate());
	return new UiLoanDateAndBooksList(date.getYear(), date.getMonthValue(), date.getDayOfMonth(),
		daysBetweenTodayAndDate(date), loanInfoBean.getBook().getTitle());
    }

    private UiLoanInfoDTO loanInfoDTOMapper(LoanInfoBean loanInfo, String language) {
	LocalDate date = LocalDate.parse(loanInfo.getReturnDate());
	return new UiLoanInfoDTO(loanInfo.getId(), daysBetweenTodayAndDate(date), localizedFullDate(date, language),
		loanInfo.getBook().getTitle(), loanInfo.getPostponed());
    }

    private Integer daysBetweenTodayAndDate(LocalDate date) {
	return (int) ChronoUnit.DAYS.between(LocalDate.now(), (date));
    }

    public List<ReservableBookExamplaryDatedBean> getReservableExamplaryBeans(String title, String buildingName,
	    Integer maxOfReservation, Integer maxPeriodLoan, String unit, String language) {
	List<ReservableBookExamplaryDatedBean> list = libraryBookLoansProxy
		.reservableBookExamplaryDTOs(getReservableBookExamplaryIds(title, buildingName, maxOfReservation),
			maxPeriodLoan, unit)
		.stream().map(o -> parseDateToLocalDate(o, language)).collect(Collectors.toList());
	return list;
    }

    private ReservableBookExamplaryDatedBean parseDateToLocalDate(ReservableBookExamplaryDatedBean bean,
	    String language) {
	bean.setClosestReturnDate(localizedFullDate(parseStringToDate(bean.getClosestReturnDate()), language));
	if (!bean.getFarrestReturnDate().equals("none")) {
	    bean.setFarrestReturnDate(localizedFullDate(parseStringToDate(bean.getFarrestReturnDate()), language));
	}
	return bean;
    }

    private List<ReservableBookLinkedLoanBean> getReservableBookLinkedLoanBeans(String title, String buildingName,
	    Integer maxOfReservation) {
	return libraryBookLoansProxy
		.getReservableBookLinkedLoans(getReservableBookExamplaryIds(title, buildingName, maxOfReservation));
    }

    private List<Integer> getReservableBookExamplaryIds(String title, String buildingName, Integer maxOfReservation) {
	List<Integer> ids = new ArrayList<>();
	getResevableBookExamplaryBeans(title, buildingName, maxOfReservation).forEach(o -> ids.add(o.getId()));
	return ids;
    }

    private List<ReservableBookExamplaryBean> getResevableBookExamplaryBeans(String title, String buildingName,
	    Integer maxOfReservation) {
	List<ReservableBookExamplaryBean> list = libraryBookCaseProxy.getReservableBooks(title, buildingName,
		maxOfReservation);
	return list;
    }

    public void createReservation(HttpServletRequest request, String headerName, Integer bookId) {
	if (isHeaderPresent(request, headerName)) {
	    String userName = userNameByToken(token(request, headerName));
	    if (userName != null) {
		libraryReservationsProxy.createReservation(bookId, userName);
	    }
	}
    }

    public void customerReservations(ModelAndView model, HttpServletRequest request, String headerName) {
	String reservationsListName = "reservationsList";
	List<UiReservationDTO> reservations = null;
	if (isHeaderPresent(request, headerName)) {
	    String userName = userNameByToken(token(request, headerName));
	    if (userName != null) {
		reservations = libraryReservationsProxy.customerReservations(userName).stream()
			.map(o -> mapReservationToDTO(o)).collect(Collectors.toList());
		if (reservations.isEmpty()) {
		    reservations = null;
		}
	    } else {
		reservations = null;
	    }
	    model.addObject(reservationsListName, reservations);
	}

    }

    private UiReservationDTO mapReservationToDTO(LibraryReservationBean bean) {
	UiReservationDTO dto = new UiReservationDTO(bean.getId(), bean.getBook().getTitle(), bean.getBuildingName(),
		null, localizedFullDate(parseStringToDate(bean.getReturnDate()), "fr"), bean.getPostpone(),
		bean.getPriority());
	if (bean.getNotificationDate() != null) {
	    dto.setNotificationDate(localizedFullDate(parseStringToDate(bean.getNotificationDate()), "fr"));
	}
	return dto;
    }

    public void cancelReservation(Integer reservationReference, String bookTitle) {
	libraryReservationsProxy.cancelReservation(reservationReference);
	libraryScheduledBatchAndMailingProxy.sendNotificationBookAvailableAfterCustomerCancel(bookTitle,
		PRIORITY_VALUE);
    }

    private Boolean isHeaderPresent(HttpServletRequest request, String headerName) {
	return request.getHeader(headerName) != null;
    }

    private String userNameByToken(String token) {
	return libraryCustomerProxy.getCustomerUserName(token);
    }

    /**
     *
     * @param localDate default format expected : yyyy-mm-dd
     * @return Default format localDate string to localDate.
     */
    private LocalDate parseStringToDate(String localDate) {
	return LocalDate.parse(localDate);
    }

    private String localizedFullDate(LocalDate date, String language) {
	Locale locale = new Locale(language);
	return date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(locale));
    }

}

package std.libraryUi.controller.controllerMethods;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import std.libraryUi.beans.LoanInfoBean;
import std.libraryUi.dto.UiLoanDateAndBooksList;
import std.libraryUi.dto.UiLoanInfoDTO;
import std.libraryUi.proxies.LibraryBookLoansProxy;

@Service
public class ControllerMethods {

	@Autowired
	LibraryBookLoansProxy libraryBookLoansProxy;

	public Boolean isUserAuthenticated() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			return true;
		} else {
			return false;
		}
	}

	public List<UiLoanInfoDTO> loanInfoDTOList(List<LoanInfoBean> list, String language) {
		dateSortedList(list);
		return list.stream().map(O -> loanInfoDTOMapper(O, language)).collect(Collectors.toList());
	}

	private void dateSortedList(List<LoanInfoBean> list) {
		list.sort((LoanInfoBean O1, LoanInfoBean O2) -> parseStringToDate(O1.getReturnDate())
				.compareTo(parseStringToDate(O2.getReturnDate())));
	}

	public void postPoneLoan(Integer loanId, String userName, Integer unitNumber, ChronoUnit unit) {
		libraryBookLoansProxy.postpone(loanId, userName, unitNumber, unit);
	}

	/**
	 *
	 * @param localDate default format expected : yyyy-mm-dd
	 * @return Default format localDate string to localDate.
	 */
	private LocalDate parseStringToDate(String localDate) {
		return LocalDate.parse(localDate);
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

	private String localizedFullDate(LocalDate date, String language) {
		Locale locale = new Locale(language);
		return date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(locale));
	}

}

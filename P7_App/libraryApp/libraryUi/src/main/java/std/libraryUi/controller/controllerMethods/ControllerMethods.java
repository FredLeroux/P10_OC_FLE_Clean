package std.libraryUi.controller.controllerMethods;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import std.libraryUi.beans.LoanInfoBean;
import std.libraryUi.dto.LoanInfoDTO;

public class ControllerMethods {




	public List<LoanInfoDTO> loanInfoDTOList(List<LoanInfoBean> list,String language) {
		return list.stream().map(O -> loanInfoDTOMapper(O,language)).collect(Collectors.toList());
	}

	private LoanInfoDTO loanInfoDTOMapper(LoanInfoBean loanInfo,String language) {
		LocalDate date = LocalDate.parse(loanInfo.getReturnDate());
		return new LoanInfoDTO(loanInfo.getId(), date.getYear(), date.getMonthValue(), date.getDayOfMonth(),
				daysBetweenTodayAndDate(date),localizedFullDate(date, language), loanInfo.getBook().getTitle(),
				loanInfo.getPostponed());
	}

	private Integer daysBetweenTodayAndDate(LocalDate date) {
		return (int)ChronoUnit.DAYS.between(LocalDate.now(),(date) );
	}

	private String localizedFullDate(LocalDate date,String language ) {
		Locale locale = new Locale(language);
		return date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(locale));
	}

}

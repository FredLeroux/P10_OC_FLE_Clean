package std.libraryUi.controller.controllerMethods;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import fleTools.date.FleToolsLocalDate;
import std.libraryUi.beans.LoanInfoBean;
import std.libraryUi.dto.LoanInfoDTO;

public class ControllerMethods {

	private FleToolsLocalDate dateMngr = new FleToolsLocalDate();


	public List<LoanInfoDTO> loanInfoDTOList(List<LoanInfoBean> list){
		return list.stream().map(O->loanInfoDTOMapper(O)).collect(Collectors.toList());
	}

	private LoanInfoDTO loanInfoDTOMapper(LoanInfoBean loanInfo) {
		LocalDate date = dateMngr.parseDate(loanInfo.getReturnDate());
		return new LoanInfoDTO(loanInfo.getId(), date.getYear(), date.getMonthValue(), date.getDayOfMonth(),
				dateMngr.daysBetweenTodayAndDate(loanInfo.getReturnDate()), loanInfo.getBook().getTitle());
	}

}

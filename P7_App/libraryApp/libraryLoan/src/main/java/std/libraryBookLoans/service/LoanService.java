package std.libraryBookLoans.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import std.libraryBookLoans.dto.LoanInfoDTO;


public interface LoanService {

	public List<LoanInfoDTO> customerLoans(String authUserName);

	/**
	 *
	 * @param loanId
	 * @param postponeDays
	 * @param daysOffList can be null
	 * @param holidays can be null
	 */
	public void postpone(Integer loanId,Integer postponeDays,ArrayList<DayOfWeek> daysOffList,ArrayList<LocalDate> holidays);


}

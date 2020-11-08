package std.libraryBookLoans.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import std.libraryBookLoans.dto.LoanInfoDTO;

public interface LoanService {

	public List<LoanInfoDTO> customerLoans(String authUserName);

	public void createLoan(Integer custommerId,Integer bookId,Integer period);

	public void returnLoan(Integer loanId, Integer custommerId);


	/**
	 *
	 * @param loanId
	 * @param postponeDays
	 * @param daysOffList  wait for a String[] containing DayOfWeek.toString(),can
	 *                     be null
	 * @param holidays     wait for a String[] containing LocalDate.toString(),can
	 *                     be null
	 * @apiNote the daysOffList and holidays as to be managed in the method owner
	 *          micro-service this two parameter allow to one day postpone if days
	 *          is an holiday or a day off Conclusion done under app not under
	 *          Ribbon and eureka try again to pass null parameters (holiday and
	 *          daysoff ) from ui microservice
	 */
	public void postponeLoan(Integer loanId,String userName, Integer postponeDays, ArrayList<DayOfWeek> daysOffList,
			ArrayList<LocalDate> holidays);



}

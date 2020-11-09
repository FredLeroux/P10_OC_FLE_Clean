package std.libraryUi.proxies;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import std.libraryUi.beans.LoanInfoBean;

@FeignClient(name = "libraryBookLoans", url = "localhost:9005")
public interface LibraryBookLoansProxy {

	@GetMapping(value = "/libraryBookLoans/loansList")
	public List<LoanInfoBean> loansList(@RequestParam(value = "userName") String userName);

	/**
	 *
	 * @param loanId
	 * @param postponeDaysNumber
	 * @param userName
	 * @param unitNumber Loan period elmt
	 * @param unit Loan period elmt
	 * @apiNote
	 * - Loan period is defined by unitNumber x unit(ChronoUnit only days/weeks/months/years)
	 *  <br>-The daysOffList and holidays as to be managed in the method owner
	 *          micro-service id not used set them to null
	 *
	 */
	@PostMapping(value = "/libraryBookLoans/postpone")
	public void postpone(@RequestParam(value = "loanId") Integer loanId,
			@RequestParam(value = "userName") String userName,
			@RequestParam(value = "unitNumber") Integer unitNumber,
			@RequestParam(value = "unit")ChronoUnit unit);



}

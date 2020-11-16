package std.libraryBookLoans.controller;

import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import std.libraryBookLoans.dto.LoanInfoDTO;
import std.libraryBookLoans.service.LoanService;

@RestController
public class LibraryBookLoansController {

	@Autowired
	LoanService loan;

	@GetMapping(value = "/loansList")
	public List<LoanInfoDTO> loansList(@RequestParam(value = "userName") String userName) {
		return loan.customerLoans(userName);
	}

	/**
	 *
	 * @param loanId
	 * @param postponeDaysNumber
	 * @param userName
	 * @param unitNumber
	 * @param unit
	 * @apiNote
	 * - period is defined by unitNumber x unit(ChronoUnit only days/weeks/months/years)
	 *  <br>-The daysOffList and holidays as to be managed in the method owner
	 *          micro-service id not used set them to null
	 *
	 */
	@PostMapping(value = "/postpone")
	public void postpone(@RequestParam(value = "loanId") Integer loanId,
			@RequestParam(value = "userName") String userName,
			@RequestParam(value = "unitNumber") Integer unitNumber,
			@RequestParam(value = "unit")ChronoUnit unit) {
		loan.postponeLoan(loanId,userName, unitNumber,unit, null, null);

	}

}

package std.libraryBookLoans.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import std.libraryBookLoans.dto.LoanInfoDTO;
import std.libraryBookLoans.service.LoanService;

@RestController
public class LibraryBookLoansController {

	@Autowired
	LoanService loan;

	@GetMapping(value = "/loansList")
	public List<LoanInfoDTO> loan(@RequestParam(value = "userName") String userName) {
		return loan.customerLoans(userName);
	}

	/**
	 *
	 * @param loanId
	 * @param postponeDaysNumber
	 * @param weekDaysOff        set null if not used
	 * @param holidays           set null if not used
	 * @apiNote postpone the loan return date in function of postponeDaysNumber,
	 *          and/or weekDaysOff(if not null) and/or holidays(if not null)
	 */
	@GetMapping(value = "/postpone")
	public void postpone(@RequestParam(value = "loanId") Integer loanId,
			@RequestParam(value = "postponeDyasNumber") Integer postponeDaysNumber,
			@RequestParam(value = "weekDaysOff") ArrayList<DayOfWeek> weekDaysOff,
			@RequestParam(value = "holidays") ArrayList<LocalDate> holidays) {
		loan.postpone(loanId, postponeDaysNumber, weekDaysOff, holidays);

	}

}

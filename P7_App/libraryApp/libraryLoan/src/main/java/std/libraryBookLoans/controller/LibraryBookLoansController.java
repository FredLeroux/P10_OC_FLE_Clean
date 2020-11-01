package std.libraryBookLoans.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.MonthDaySerializer;

import std.libraryBookLoans.dto.LoanInfoDTO;
import std.libraryBookLoans.entities.Loan;
import std.libraryBookLoans.service.LoanService;

@RestController
public class LibraryBookLoansController {

	@Autowired
	LoanService loan;

	@GetMapping(value="loan")
	public List<LoanInfoDTO> loan(){
		return loan.customerLoans("mail");
	}

	@GetMapping(value="postpone")
	public void postpone(){
		System.out.println(DayOfWeek.SUNDAY);
		DayOfWeek[] array = {DayOfWeek.SUNDAY,DayOfWeek.MONDAY};
		LocalDate[] publicHoliday = {LocalDate.parse("2020-11-17")};
		loan.postpone(1,14,null	,null);

	}




}

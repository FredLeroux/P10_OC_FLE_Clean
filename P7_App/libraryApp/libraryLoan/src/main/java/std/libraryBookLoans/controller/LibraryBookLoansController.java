package std.libraryBookLoans.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
		loan.pospone(1);

	}


}

package std.libraryBookLoans.service;

import java.util.List;

import std.libraryBookLoans.dto.LoanInfoDTO;
import std.libraryBookLoans.entities.Loan;

public interface LoanService {

	public List<LoanInfoDTO> customerLoans(String authUserName);

	public void pospone(Integer loanId);


}

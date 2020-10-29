package std.libraryBookLoans.service;


import java.util.List;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import fleTools.date.FleToolsLocalDate;
import std.libraryBookLoans.dao.CustomerLoanDAO;
import std.libraryBookLoans.dao.LoanDAO;
import std.libraryBookLoans.dto.LoanInfoDTO;
import std.libraryBookLoans.entities.Loan;

@Service
public class LoanServiceImpl implements LoanService {

	@Autowired
	LoanDAO loanDAO;

	@Autowired
	CustomerLoanDAO cust;

	private FleToolsLocalDate date = new FleToolsLocalDate();
	private ModelMapper mapper = new ModelMapper();
	private LoanInfoDTO loanInfoDTO = new LoanInfoDTO();





	@Override
	public List<LoanInfoDTO> customerLoans(String authUserName) {
		Integer customerId = cust.findOneByCustomerEmail(authUserName).get().getId();
		List<Loan> list = loanDAO.findByCustomerIdAndReturnedFalse(customerId);
		return (List<LoanInfoDTO>) list.stream().map(O->mapping(O,loanInfoDTO)).collect(Collectors.toList());
	}


	@Override
	public void pospone(Integer loanId) {
		Loan loan = new Loan();
		if(loanDAO.findById(1).isPresent()) {
			loan = loanDAO.findById(1).get();
			loan.setReturnDate(date.addDays(loan.getReturnDate(),14).toString());
			loan.setPostponed(true);
		}
		 loanDAO.saveAndFlush(loan);
	}


	@SuppressWarnings("unchecked")
	private<O extends Object> O mapping(Object source, O destination){
		return (O) mapper.map(source, destination.getClass());
	}



}

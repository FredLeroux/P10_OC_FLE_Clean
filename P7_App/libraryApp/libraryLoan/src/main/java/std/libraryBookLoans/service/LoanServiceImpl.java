package std.libraryBookLoans.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	private ModelMapper mapper = new ModelMapper();
	private LoanInfoDTO loanInfoDTO = new LoanInfoDTO();

	@Override
	public List<LoanInfoDTO> customerLoans(String authUserName) {
		Integer customerId = cust.findOneByCustomerEmail(authUserName).get().getId();
		List<Loan> list = loanDAO.findByCustomerIdAndReturnedFalse(customerId);
		return (List<LoanInfoDTO>) list.stream().map(O -> mapping(O, loanInfoDTO)).collect(Collectors.toList());
	}

	@Override
	public void postpone(Integer loanId, Integer postponeDays, ArrayList<DayOfWeek> daysOffList,
			ArrayList<LocalDate> holidays) {
		Loan loan = new Loan();
		if (loanDAO.findById(1).isPresent()) {
			loan = loanDAO.findById(1).get();
			loan.setReturnDate(
					posponeDaysOffTakedInAccount(loan.getReturnDate(), postponeDays, daysOffList, holidays).toString());
			loan.setPostponed(true);
		}
		loanDAO.saveAndFlush(loan);
	}

	@SuppressWarnings("unchecked")
	private <O extends Object> O mapping(Object source, O destination) {
		return (O) mapper.map(source, destination.getClass());
	}

	private LocalDate posponeDaysOffTakedInAccount(String date, Integer daysToAdd, ArrayList<DayOfWeek> daysOffList,
			ArrayList<LocalDate> holidays) {
		List<DayOfWeek> daysOff = daysOffList != null ? daysOffList : Collections.emptyList();
		List<LocalDate> holidayList = holidays != null ? holidays : Collections.emptyList();
		Boolean isDayOff = true;
		System.out.println(date);
		LocalDate postponed = postponedDate(date, daysToAdd);
		System.out.println(daysOff.contains(postponed.getDayOfWeek()) || holidayList.contains(postponed));
		while (isDayOff) {
			System.out.println(postponed);
			if (daysOff.contains(postponed.getDayOfWeek()) || holidayList.contains(postponed)) {
				postponed = postponed.plusDays(1);
			} else {
				isDayOff = false;
			}
		}
		System.out.println(postponed.toString());
		return postponed;
	}

	private LocalDate postponedDate(String date, Integer daysToAdd) {
		return LocalDate.parse(date).plusDays(daysToAdd);
	}

}

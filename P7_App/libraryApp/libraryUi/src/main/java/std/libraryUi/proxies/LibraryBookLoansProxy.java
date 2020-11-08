package std.libraryUi.proxies;

import java.time.DayOfWeek;
import java.time.LocalDate;
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

	@PostMapping(value = "/libraryBookLoans/postpone")
	public void postpone(@RequestParam(value = "loanId") Integer loanId,
			@RequestParam(value = "userName") String userName,
			@RequestParam(value = "postponeDaysNumber") Integer postponeDaysNumber);

}

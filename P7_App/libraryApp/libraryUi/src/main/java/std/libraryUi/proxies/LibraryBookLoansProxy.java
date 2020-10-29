package std.libraryUi.proxies;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import std.libraryUi.beans.LoanInfoBean;

@FeignClient(name ="libraryBookLoans", url ="localhost:9005" )
public interface LibraryBookLoansProxy {

	@GetMapping(value="/libraryBookLoans/loan")///libraryBookLoans
	public List<LoanInfoBean> loan();

}

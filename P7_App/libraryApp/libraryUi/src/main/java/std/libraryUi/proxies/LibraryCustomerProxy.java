package std.libraryUi.proxies;

import java.util.Optional;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import std.libraryUi.beans.LibraryCustomerLogBean;

@FeignClient(name = "libraryCustomers", url = "localhost:9003")
public interface LibraryCustomerProxy {

	@GetMapping(value = "/getCustomerLog")
	public Optional<LibraryCustomerLogBean> getCustomer(@RequestParam(value = "username") String customerEmail);

}

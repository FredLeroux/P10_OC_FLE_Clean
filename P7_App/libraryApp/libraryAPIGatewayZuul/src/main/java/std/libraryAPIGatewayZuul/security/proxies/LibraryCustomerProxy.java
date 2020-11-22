package std.libraryAPIGatewayZuul.security.proxies;

import java.util.Optional;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import std.libraryAPIGatewayZuul.security.beans.LibraryCustomerLogBean;

@FeignClient(name = "libraryCustomers")
@RibbonClient(name = "libraryCustomers")
public interface LibraryCustomerProxy {

	@GetMapping(value = "/getCustomerLog")
	public Optional<LibraryCustomerLogBean> getCustomer(@RequestParam(value = "username") String customerEmail);

	@PostMapping(value = "/authToken")
	public void authToken(@RequestParam(value = "userName") String userName,
			@RequestParam(value = "token") String token);

}

package std.libraryAPIGatewayZuul.security.proxies;

import java.util.Optional;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import std.libraryAPIGatewayZuul.security.beans.LibraryCustomerLogBean;

@FeignClient(name = "gateway-zuul")
@RibbonClient(name = "libraryCustomers")
public interface LibraryCustomerProxy {

	@GetMapping(value = "/libraryCustomers/getCustomerLog") // libraryCustomers
	public Optional<LibraryCustomerLogBean> getCustomer(@RequestParam(value = "username") String customerEmail);

	@PostMapping(value = "/libraryCustomers/authToken")
	public void authToken(@RequestParam(value = "userName") String userName,
			@RequestParam(value = "token") String token);

}

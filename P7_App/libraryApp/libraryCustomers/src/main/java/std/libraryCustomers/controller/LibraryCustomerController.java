package std.libraryCustomers.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import std.libraryCustomers.dto.CustomerLogDTO;

import std.libraryCustomers.service.libraryCustomersService.LibraryCustomersService;


@RestController
public class LibraryCustomerController {

	@Autowired
	private LibraryCustomersService serviceCustomer;

	@GetMapping("/save")
	public void saveCustommer() {
	}

	@GetMapping("/getCustomerLog")
	public CustomerLogDTO getCustomer(@RequestParam(value = "username") String customerEmail) {
		CustomerLogDTO dto = serviceCustomer.findByCustomerEmail(customerEmail);
		return dto;
	}

	@GetMapping("/getCustomer")
	public String getCustomerUserName(@RequestParam(value = "token") String token) {
		return serviceCustomer.findByAuthToken(token).getCustomerEmail().toString();
	}

	@PostMapping("/authToken")
	public void authToken(@RequestParam(value = "userName") String userName,
			@RequestParam(value = "token") String token) {
		serviceCustomer.addAuth(userName, token);
	}

}

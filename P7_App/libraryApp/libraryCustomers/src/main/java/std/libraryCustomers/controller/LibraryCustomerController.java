package std.libraryCustomers.controller;

import org.hibernate.internal.build.AllowSysOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import std.libraryCustomers.dto.CustomerLogDTO;
import std.libraryCustomers.dto.CustomerSaveDTO;
import std.libraryCustomers.service.libraryCustomersService.LibraryCustomersService;
import std.libraryCustomers.service.libraryRolesService.LibraryRolesService;

@RestController
public class LibraryCustomerController {

	@Autowired
	private LibraryCustomersService serviceCustomer;

	@Autowired
	private  LibraryRolesService serviceRoles;

	@GetMapping("/save")
	public void saveCustommer() {
		CustomerSaveDTO dto = new CustomerSaveDTO();
		//TODO Suppress
		dto.setCustomerEmail("mail");
		//TODO Suppress
		dto.setCustomerPassword("pass");
		dto.setCustomerEnabled(true);
		dto.setCustomerAccountNonLocked(true);
		dto.setCustomerCredentialNonExpired(true);
		dto.setCustomerAccountNonExpired(true);
		dto.setRoles(serviceRoles.getRoleById(1).get());
		serviceCustomer.saveCustommer(dto);
	}

	@GetMapping("/getCustomerLog")
	public CustomerLogDTO getCustomer(@RequestParam(value = "username") String customerEmail) {
		System.out.println("log");
		CustomerLogDTO dto = serviceCustomer.findByCustomerEmail(customerEmail);
		return dto;
	}

	@PostMapping("/authToken")
	public void authToken(@RequestParam(value = "userName") String userName, @RequestParam(value="token") String token) {
		System.out.println("tokencontroller");
		serviceCustomer.addAuth(userName, token);
	}

}

package std.libraryCustomers.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import std.libraryCustomers.dto.CustomerDTO;
import std.libraryCustomers.dto.CustomerLogDTO;
import std.libraryCustomers.service.libraryCustomersService.LibraryCustomersService;

@RestController
public class LibraryCustomerController {

    @Autowired
    private LibraryCustomersService serviceCustomer;

    @GetMapping("/save")
    public void saveCustommer() {
    }

    /**
     *
     * @param customerEmail is the userName
     * @return Customer loggin information
     */
    @GetMapping("/getCustomerLog")
    public CustomerLogDTO getCustomerLog(@RequestParam(value = "username") String customerEmail) {
	CustomerLogDTO dto = serviceCustomer.getCustomerLogInfoByCustomerEmail(customerEmail);
	return dto;
    }

    @GetMapping("/getCustomerFull")
    public CustomerDTO getCustomerFull(@RequestParam(value = "username") String customerEmail) {
	CustomerDTO dto = serviceCustomer.getCustomerByEmail(customerEmail);
	return dto;
    }

    @GetMapping("/getCustomerUserName")
    public String getCustomerUserName(@RequestParam(value = "token") String token) {
	return serviceCustomer.findByAuthToken(token).getCustomerEmail().toString();
    }

    @PostMapping("/authToken")
    public void authToken(@RequestParam(value = "userName") String userName,
	    @RequestParam(value = "token") String token) {
	serviceCustomer.addAuth(userName, token);
    }

}

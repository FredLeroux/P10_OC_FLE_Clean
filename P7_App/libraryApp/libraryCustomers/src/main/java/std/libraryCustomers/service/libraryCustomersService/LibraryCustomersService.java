package std.libraryCustomers.service.libraryCustomersService;

import std.libraryCustomers.dto.CustomerLogDTO;
import std.libraryCustomers.dto.CustomerSaveDTO;
import std.libraryCustomers.dto.CustomerUserNameDTO;

public interface LibraryCustomersService {

	public CustomerLogDTO findByCustomerEmail(String customerEmail);

	public CustomerUserNameDTO findByAuthToken(String authToken);

	public void addAuth(String userName,String token);

	public void saveCustommer(CustomerSaveDTO customerDTO);

}

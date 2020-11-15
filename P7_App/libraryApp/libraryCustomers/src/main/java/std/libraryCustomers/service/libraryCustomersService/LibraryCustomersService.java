package std.libraryCustomers.service.libraryCustomersService;

import std.libraryCustomers.dto.CustomerLogDTO;
import std.libraryCustomers.dto.CustomerSaveDTO;

public interface LibraryCustomersService {

	public CustomerLogDTO findByCustomerEmail(String customerEmail);

	public void addAuth(String userName,String token);

	public void saveCustommer(CustomerSaveDTO customerDTO);

}

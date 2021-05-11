package std.libraryCustomers.service.libraryCustomersService;

import std.libraryCustomers.dto.CustomerDTO;
import std.libraryCustomers.dto.CustomerLogDTO;
import std.libraryCustomers.dto.CustomerUserNameDTO;

public interface LibraryCustomersService {

    public CustomerDTO getCustomerByEmail(String customerEmail);

    public CustomerLogDTO getCustomerLogInfoByCustomerEmail(String customerEmail);

    public CustomerUserNameDTO findByAuthToken(String authToken);

    public void addAuth(String userName, String token);

    public void saveCustommer(CustomerDTO customerDTO);

}

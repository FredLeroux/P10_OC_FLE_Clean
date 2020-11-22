package std.libraryCustomers.service.libraryCustomersService;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import std.libraryCustomers.dao.LibraryCustomersDao;
import std.libraryCustomers.dao.LibraryRolesDAO;
import std.libraryCustomers.dto.CustomerLogDTO;
import std.libraryCustomers.dto.CustomerSaveDTO;
import std.libraryCustomers.dto.CustomerUserNameDTO;
import std.libraryCustomers.dto.LibraryRoleDTO;
import std.libraryCustomers.entities.Customer;
import std.libraryCustomers.entities.LibraryRole;


@Service
public class LibraryCustomersServiceImpl implements LibraryCustomersService {

	@Autowired
	LibraryCustomersDao dao;

	@Autowired
	LibraryRolesDAO daoRole;

	private ModelMapper mapper = new ModelMapper();
	private Customer customer = new Customer();
	private CustomerLogDTO customerLogDTO = new CustomerLogDTO();
	private CustomerUserNameDTO customerUserNameDTO =new CustomerUserNameDTO();
	private LibraryRoleDTO libraryRoleDTO = new LibraryRoleDTO();

	@Override
	public CustomerLogDTO findByCustomerEmail(String customerEmail) {
		if(dao.findByCustomerEmail(customerEmail).isPresent()) {
		Customer customer = dao.findByCustomerEmail(customerEmail).get();
		CustomerLogDTO dto = (CustomerLogDTO) customerToCustomerDTO(customer,customerLogDTO);
		LibraryRoleDTO roleDTO = libraryRoleToLibraryRoleDTO(customer.getRole());
		dto.setRole(roleDTO);
		return dto;
		}
		return null;
	}


	@Override
	public CustomerUserNameDTO findByAuthToken(String authToken) {
		if(dao.findByCustomerAuthToken(authToken).isPresent()) {
			Customer customer = dao.findByCustomerAuthToken(authToken).get();
			CustomerUserNameDTO dto = mapper(customer,customerUserNameDTO);
			return dto;
			}
			return null;
		}


	@Override
	public void saveCustommer(CustomerSaveDTO customerDTO) {
		dao.save(customerDTOToCustomer(customerDTO));

	}

	private Customer customerDTOToCustomer(Object customerDTO) {
		return (Customer) mapper(customerDTO,customer);
	}

	private Object customerToCustomerDTO(Customer customer,Object customerDTO) {
		return mapper(customer,customerDTO);
	}

	private LibraryRoleDTO libraryRoleToLibraryRoleDTO(LibraryRole role) {
		return mapper(role,libraryRoleDTO);
	}

	@SuppressWarnings("unchecked")
	private <O extends Object> O mapper(Object source, O destination){
		return (O) mapper.map(source, destination.getClass());
	}

	@Override
	public void addAuth(String userName, String token) {
		Optional<Customer> optCustomer = dao.findByCustomerEmail(userName);
		if(optCustomer.isPresent()) {
			Customer customer = optCustomer.get();
			customer.setCustomerAuthToken(token);
			dao.save(customer);
	}
	}

}

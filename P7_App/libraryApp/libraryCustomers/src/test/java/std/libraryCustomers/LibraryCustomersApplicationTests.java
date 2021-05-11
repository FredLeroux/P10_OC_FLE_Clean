package std.libraryCustomers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import std.libraryCustomers.dao.LibraryCustomersDao;
import std.libraryCustomers.dao.LibraryRolesDAO;
import std.libraryCustomers.dto.CustomerDTO;
import std.libraryCustomers.dto.LibraryRoleDTO;
import std.libraryCustomers.entities.Customer;
import std.libraryCustomers.entities.LibraryRole;
import std.libraryCustomers.exceptions.AuthTokenNotFoundException;
import std.libraryCustomers.exceptions.CustomerNotFoundException;
import std.libraryCustomers.service.libraryCustomersService.LibraryCustomersServiceImpl;

@SpringBootTest(classes = { LibraryCustomersServiceImpl.class })
@TestPropertySource(locations = "/application-unitTest.properties")
@ExtendWith(MockitoExtension.class)
class LibraryCustomersApplicationTests {

    @MockBean
    private LibraryCustomersDao dao;

    @MockBean
    private LibraryRolesDAO daoRole;

    @InjectMocks
    private LibraryCustomersServiceImpl service = new LibraryCustomersServiceImpl();

    private Customer customer;
    private LibraryRole role;
    private CustomerDTO customerDTO;
    private LibraryRoleDTO roleDTO;

    @BeforeEach
    public void init() {
	role = new LibraryRole(1, "type");
	roleDTO = new LibraryRoleDTO(1, "type");
	customer = new Customer(1, "customerEmail", "customerPassword", true, true, true, true, "customerAuthToken",
		role);
	customerDTO = new CustomerDTO(1, "customerEmail", "customerPassword", true, true, true, true, roleDTO);
    }

    @Test
    public void customerTest() {
	when(dao.findByCustomerEmail(ArgumentMatchers.any())).thenReturn(Optional.of(customer));
	when(dao.findByCustomerAuthToken(ArgumentMatchers.any())).thenReturn(Optional.of(customer));
	assertThatCode(() -> service.getCustomerByEmail("customerEmail")).doesNotThrowAnyException();
	assertThatCode(() -> service.getCustomerLogInfoByCustomerEmail("customerEmail")).doesNotThrowAnyException();
	assertThatCode(() -> service.findByAuthToken("customerEmail")).doesNotThrowAnyException();
    }

    @Test
    public void customerTestFail() {
	when(dao.findByCustomerEmail(ArgumentMatchers.any())).thenReturn(Optional.empty());
	when(dao.findByCustomerAuthToken(ArgumentMatchers.any())).thenReturn(Optional.empty());
	assertThatThrownBy(() -> service.getCustomerByEmail("customerEmail"))
		.isInstanceOf(CustomerNotFoundException.class);
	assertThatThrownBy(() -> service.getCustomerLogInfoByCustomerEmail("customerEmail"))
		.isInstanceOf(CustomerNotFoundException.class);
	assertThatThrownBy(() -> service.findByAuthToken("customerEmail"))
		.isInstanceOf(AuthTokenNotFoundException.class);
    }

    @Test
    public void addAuthTest() {
	when(dao.findByCustomerEmail(ArgumentMatchers.any())).thenReturn(Optional.of(customer));
	service.addAuth("userName", "token");
	assertThat(customer.getCustomerAuthToken()).isEqualTo("token");
	verify(dao, times(1)).save(ArgumentMatchers.any(Customer.class));

    }

    @Test
    public void addAuthTestNoCustomer() {
	when(dao.findByCustomerEmail(ArgumentMatchers.any())).thenReturn(Optional.empty());
	service.addAuth("userName", "token");
	assertThat(customer.getCustomerAuthToken()).isEqualTo(customer.getCustomerAuthToken());
	verify(dao, times(0)).save(ArgumentMatchers.any(Customer.class));
    }

    @Test
    public void SaveCustomerTest() {
	assertThatCode(() -> service.saveCustommer(customerDTO)).doesNotThrowAnyException();
    }

    @Test
    public void SaveCustomerTestFail() {
	assertThatThrownBy(() -> service.saveCustommer(null)).isInstanceOfAny(IllegalArgumentException.class);
	customerDTO.setCustomerEmail(null);

    }

}

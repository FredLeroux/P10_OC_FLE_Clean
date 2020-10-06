package std.libraryCustomers.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import std.libraryCustomers.entities.Customer;

@Repository
public interface LibraryCustomersDao extends JpaRepository<Customer, Integer>{

	public Customer findByCustomerEmail(String customerEmail);

}

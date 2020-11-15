package std.libraryCustomers.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import std.libraryCustomers.entities.Customer;
import std.libraryCustomers.entities.LibraryRole;

@Repository
public interface LibraryCustomersDao extends JpaRepository<Customer, Integer>{

	public Optional<Customer> findByCustomerEmail(String customerEmail);

	public Optional<Customer> findByCustomerAuthToken(String customerAuthToken);




}

package std.libraryCustomers.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import std.libraryCustomers.entities.Customer;

@Repository
public interface LibraryCustomersDAO extends JpaRepository<Customer, Integer>{

	public Optional<Customer> findByCustomerEmail(String customerEmail);

	public Optional<Customer> findByCustomerAuthToken(String customerAuthToken);




}

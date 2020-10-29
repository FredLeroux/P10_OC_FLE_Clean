package std.libraryBookLoans.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import std.libraryBookLoans.entities.CustomerLoan;

@Repository
public interface CustomerLoanDAO extends JpaRepositoryImplementation<CustomerLoan, Integer> {


	public Optional<CustomerLoan> findOneByCustomerEmail(String customerEmail);

}

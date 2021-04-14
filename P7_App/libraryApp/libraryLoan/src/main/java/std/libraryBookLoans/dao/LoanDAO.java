package std.libraryBookLoans.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import std.libraryBookLoans.entities.Loan;

@Repository
public interface LoanDAO extends JpaRepositoryImplementation<Loan, Integer> {

    public Optional<Loan> findByIdAndCustomerCustomerEmail(Integer id, String customerEmail);

    public Optional<Loan> findByBookIdAndCustomerIdAndReturnedFalse(Integer bookId, Integer customerId);

    public Optional<Loan> findByBookTitleAndCustomerIdAndReturnedFalse(String bookTitle, Integer customerId);

    public List<Loan> findByCustomerIdAndReturnedFalse(Integer id);

    public List<Loan> findByReturnedFalseAndBookIdIn(List<Integer> bookId);

}

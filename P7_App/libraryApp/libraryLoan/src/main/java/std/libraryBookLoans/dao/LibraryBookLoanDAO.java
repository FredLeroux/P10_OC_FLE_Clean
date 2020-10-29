package std.libraryBookLoans.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import std.libraryBookLoans.entities.LibraryBookLoan;

@Repository
public interface LibraryBookLoanDAO extends JpaRepositoryImplementation<LibraryBookLoan, Integer> {

	public Optional<LibraryBookLoan> findById(Integer id);



}

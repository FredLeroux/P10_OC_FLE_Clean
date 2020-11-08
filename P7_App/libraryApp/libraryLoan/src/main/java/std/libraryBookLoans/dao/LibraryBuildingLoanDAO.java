package std.libraryBookLoans.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import std.libraryBookLoans.entities.LibraryBuildingLoan;



public interface LibraryBuildingLoanDAO extends JpaRepositoryImplementation<LibraryBuildingLoan, Integer> {

	public Optional<LibraryBuildingLoan> findById(Integer id);

}

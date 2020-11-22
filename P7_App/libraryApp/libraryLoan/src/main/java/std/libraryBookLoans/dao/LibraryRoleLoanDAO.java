package std.libraryBookLoans.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import std.libraryBookLoans.entities.LibraryRoleLoan;

public interface LibraryRoleLoanDAO extends JpaRepositoryImplementation<LibraryRoleLoan, Integer>{

	public Optional<LibraryRoleLoan> findById(Integer id);


}

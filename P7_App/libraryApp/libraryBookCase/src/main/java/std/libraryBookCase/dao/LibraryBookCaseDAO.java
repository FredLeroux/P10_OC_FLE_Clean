package std.libraryBookCase.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import std.libraryBookCase.entities.Books;

@Repository
public interface LibraryBookCaseDAO extends JpaRepository<Books, Integer> {

}

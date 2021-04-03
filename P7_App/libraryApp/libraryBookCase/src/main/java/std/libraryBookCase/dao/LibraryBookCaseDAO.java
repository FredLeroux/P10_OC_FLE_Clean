package std.libraryBookCase.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import std.libraryBookCase.entities.LibraryBook;

@Repository
public interface LibraryBookCaseDAO extends JpaRepository<LibraryBook, Integer> {

	public List<LibraryBook>  findAll();

	public List<LibraryBook> findByLibraryBuildingIdAndAvailabilityTrue(Integer id);

	public List<LibraryBook> findByKindInAndAvailabilityTrue(List<String> kinds);

	public List<LibraryBook> findByLibraryBuildingIdAndKindInAndAvailabilityTrue(Integer id,List<String> kinds);

}

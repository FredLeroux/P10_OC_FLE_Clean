package std.libraryBookCase.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import std.libraryBookCase.entities.Books;

@Repository
public interface LibraryBookCaseDAO extends JpaRepository<Books, Integer> {

	public List<Books> findByAvailabilityTrue();

	public List<Books> findByLibraryBuildingIdAndAvailabilityTrue(Integer id);

	public List<Books> findByKindInAndAvailabilityTrue(List<String> kinds);

	public List<Books> findByLibraryBuildingIdAndKindInAndAvailabilityTrue(Integer id,List<String> kinds);

}

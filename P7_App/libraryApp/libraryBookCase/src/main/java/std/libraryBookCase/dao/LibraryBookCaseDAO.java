package std.libraryBookCase.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import std.libraryBookCase.entities.LibraryBook;

@Repository
public interface LibraryBookCaseDAO extends JpaRepository<LibraryBook, Integer> {

    @Override
    public Optional<LibraryBook> findById(Integer id);

    @Override
    public List<LibraryBook> findAll();

    public List<LibraryBook> findByLibraryBuildingId(Integer id);

    public List<LibraryBook> findByKindIn(List<String> kinds);

    public List<LibraryBook> findByLibraryBuildingIdAndKindIn(Integer id, List<String> kinds);

    public List<LibraryBook> findByTitleAndLibraryBuildingNameAndNumberOfReservationsLessThan(String title,
	    String buildingName, Integer maxOfReservation);

}

package std.libraryBookCase.service;

import java.util.List;

import std.libraryBookCase.dto.LibraryBookAndQuantityDTO;
import std.libraryBookCase.dto.LibraryBooksKindsDTO;
import std.libraryBookCase.dto.LibraryReservableBookExamplary;

public interface LibraryBookCaseService {

    public List<LibraryBookAndQuantityDTO> getAllBooks(Integer maxReservationNumber);

    public List<LibraryBooksKindsDTO> getKindsList();

    public List<LibraryBookAndQuantityDTO> getBooksFilteredByBuilding(Integer id, Integer maxReservationNumber);

    public List<LibraryBookAndQuantityDTO> getBooksFilteredByKinds(List<String> kinds, Integer maxReservationNumber);

    public List<LibraryBookAndQuantityDTO> getBooksFilteredByLibraryBuildingIdAndKinds(Integer id, List<String> kinds,
	    Integer maxReservationNumber);

    public List<LibraryReservableBookExamplary> getReservableBooks(String title, String buildingName,
	    Integer maxOfReservation);
}

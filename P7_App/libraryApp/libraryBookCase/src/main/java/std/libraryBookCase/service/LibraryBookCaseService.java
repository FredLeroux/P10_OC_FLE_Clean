package std.libraryBookCase.service;

import java.util.List;

import std.libraryBookCase.dto.LibraryBooksAndQuantityDTO;
import std.libraryBookCase.dto.LibraryBooksKindsDTO;


public interface LibraryBookCaseService {

	public List<LibraryBooksAndQuantityDTO> getAllBooks();

	public List<LibraryBooksKindsDTO> getKindsList();

	public List<LibraryBooksAndQuantityDTO> getBooksFilteredByBuilding(Integer id);

	public List<LibraryBooksAndQuantityDTO> getBooksFilteredByKinds(List<String> kinds);

	public List<LibraryBooksAndQuantityDTO> getBooksFilteredByLibraryBuildingIdAndKinds(Integer id,List<String> kinds);

}

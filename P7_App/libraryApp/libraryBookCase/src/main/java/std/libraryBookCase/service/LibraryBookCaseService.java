package std.libraryBookCase.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import std.libraryBookCase.dto.LibraryBooksAndQuantityDTO;
import std.libraryBookCase.dto.LibraryBooksKindsDTO;
import std.libraryBookCase.entities.LibraryBook;


public interface LibraryBookCaseService {

	public List<LibraryBooksAndQuantityDTO> getAllBooks();

	public List<LibraryBooksKindsDTO> getKindsList();

	public List<LibraryBooksAndQuantityDTO> getBooksFilteredByBuilding(Integer id);

	public List<LibraryBooksAndQuantityDTO> getBooksFilteredByKinds(List<String> kinds);

	public List<LibraryBooksAndQuantityDTO> getBooksFilteredByLibraryBuildingIdAndKinds(Integer id,List<String> kinds);

}

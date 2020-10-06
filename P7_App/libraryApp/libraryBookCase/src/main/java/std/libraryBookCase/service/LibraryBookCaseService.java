package std.libraryBookCase.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import std.libraryBookCase.dto.BooksAndQuantityDTO;
import std.libraryBookCase.dto.BooksKindsDTO;
import std.libraryBookCase.entities.Books;


public interface LibraryBookCaseService {

	public List<BooksAndQuantityDTO> getAllBooks();

	public List<BooksKindsDTO> getKindsList();

	public List<BooksAndQuantityDTO> getBooksFilteredByBuilding(Integer id);

	public List<BooksAndQuantityDTO> getBooksFilteredByKinds(List<String> kinds);

	public List<BooksAndQuantityDTO> getBooksFilteredByLibraryBuildingIdAndKinds(Integer id,List<String> kinds);

}

package std.libraryBookCase.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import std.libraryBookCase.dto.BooksAndQuantityDTO;
import std.libraryBookCase.dto.BooksDTO;
import std.libraryBookCase.dto.BooksKindsDTO;
import std.libraryBookCase.service.LibraryBookCaseService;

@RestController
public class LibraryBookCaseController {

	@Autowired
	LibraryBookCaseService libraryBookCaseService;

	@GetMapping(value = "/books")
	public List<BooksAndQuantityDTO> books() {
		return libraryBookCaseService.getAllBooks();

	}

	@GetMapping(value = "/kinds")
	public List<BooksKindsDTO> kinds() {
		return libraryBookCaseService.getKindsList();
	}

	@GetMapping(value = "/buildingFiltered")
	public List<BooksAndQuantityDTO> booksBuildingFiltered(
			@RequestParam(name = "libraryBuilding") Integer libraryBuilding) {
		return libraryBookCaseService.getBooksFilteredByBuilding(libraryBuilding);
	}

	@GetMapping(value = "/kindsFiltered")
	public List<BooksAndQuantityDTO> booksKindsFiltered(@RequestParam(name = "kinds") List<String> kinds) {
		return libraryBookCaseService.getBooksFilteredByKinds(kinds);
	}

	@GetMapping(value = "/buildingAndKindsFiltered")
	public List<BooksAndQuantityDTO> booksBuildingAndKindsFiltered(
			@RequestParam(name = "libraryBuilding") Integer libraryBuilding,
			@RequestParam(name = "kinds") List<String> kinds) {
		return libraryBookCaseService.getBooksFilteredByLibraryBuildingIdAndKinds(libraryBuilding, kinds);
	}

}

package std.libraryBookCase.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import std.libraryBookCase.dto.LibraryBookAndQuantityDTO;
import std.libraryBookCase.dto.LibraryBooksKindsDTO;
import std.libraryBookCase.service.LibraryBookCaseService;

@RestController
public class LibraryBookCaseController {

    @Autowired
    LibraryBookCaseService libraryBookCaseService;

    @GetMapping(value = "/books")
    public List<LibraryBookAndQuantityDTO> books(
	    @RequestParam(name = "maxReservationNumber") Integer maxReservationNumber) {
	return libraryBookCaseService.getAllBooks(maxReservationNumber);

    }

    @GetMapping(value = "/kinds")
    public List<LibraryBooksKindsDTO> kinds() {
	return libraryBookCaseService.getKindsList();
    }

    @GetMapping(value = "/buildingFiltered")
    public List<LibraryBookAndQuantityDTO> booksBuildingFiltered(
	    @RequestParam(name = "libraryBuilding") Integer libraryBuilding,
	    @RequestParam(name = "maxReservationNumber") Integer maxReservationNumber) {
	return libraryBookCaseService.getBooksFilteredByBuilding(libraryBuilding, maxReservationNumber);
    }

    @GetMapping(value = "/kindsFiltered")
    public List<LibraryBookAndQuantityDTO> booksKindsFiltered(@RequestParam(name = "kinds") List<String> kinds,
	    @RequestParam(name = "maxReservationNumber") Integer maxReservationNumber) {
	return libraryBookCaseService.getBooksFilteredByKinds(kinds, maxReservationNumber);
    }

    @GetMapping(value = "/buildingAndKindsFiltered")
    public List<LibraryBookAndQuantityDTO> booksBuildingAndKindsFiltered(
	    @RequestParam(name = "libraryBuilding") Integer libraryBuilding,
	    @RequestParam(name = "kinds") List<String> kinds,
	    @RequestParam(name = "maxReservationNumber") Integer maxReservationNumber) {
	return libraryBookCaseService.getBooksFilteredByLibraryBuildingIdAndKinds(libraryBuilding, kinds,
		maxReservationNumber);
    }

}

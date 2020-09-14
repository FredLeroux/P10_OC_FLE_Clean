package std.libraryBookCase.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import std.libraryBookCase.entities.Books;
import std.libraryBookCase.service.LibraryBookCaseService;

@RestController
public class LibraryBookCaseController {

	@Autowired
	LibraryBookCaseService libraryBookCaseService;

	@GetMapping(value = "books")
	public List<Books> books(){
		return libraryBookCaseService.getAllBooks();
	}

}

package std.libraryBookCase.service;

import java.util.List;

import org.springframework.stereotype.Service;

import std.libraryBookCase.entities.Books;


public interface LibraryBookCaseService {

	public List<Books> getAllBooks();

}

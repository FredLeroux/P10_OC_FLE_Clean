package std.libraryBookCase.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import std.libraryBookCase.dao.LibraryBookCaseDAO;
import std.libraryBookCase.entities.Books;

@Service
public class LibraryBookCaseServiceImpl implements LibraryBookCaseService {

	@Autowired
	LibraryBookCaseDAO dao;

	@Override
	public List<Books> getAllBooks() {
		return dao.findAll();
	}

}

package std.libraryBookCase.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import std.libraryBookCase.dao.LibraryBookCaseDAO;
import std.libraryBookCase.dto.LibraryBookAndQuantityDTO;
import std.libraryBookCase.dto.LibraryBookDTO;
import std.libraryBookCase.dto.LibraryBooksKindsDTO;
import std.libraryBookCase.dto.LibraryBuildingForBookDTO;
import std.libraryBookCase.dto.LibraryReservableBookExamplary;
import std.libraryBookCase.entities.LibraryBook;
import std.libraryBookCase.entities.LibraryBuilding;
import std.libraryBookCase.exceptions.BookNotFoundException;

@Service
public class LibraryBookCaseServiceImpl implements LibraryBookCaseService {

    @Autowired
    private LibraryBookCaseDAO dao;

    private ModelMapper modelMapper = new ModelMapper();
    private LibraryBookDTO bookDTO = new LibraryBookDTO();
    private LibraryBooksKindsDTO booksKindsDTO = new LibraryBooksKindsDTO();
    private LibraryBookAndQuantityDTO booksAndQuantityDTO = new LibraryBookAndQuantityDTO();
    private LibraryReservableBookExamplary reservableBook = new LibraryReservableBookExamplary();
    private LibraryBuildingForBookDTO libraryBuildingForBookDTO = new LibraryBuildingForBookDTO();

    @Override
    public List<LibraryBookAndQuantityDTO> getAllBooks(Integer maxReservationNumber) {
	return booksAndQuantityList(booksDTOList(dao.findAll()), maxReservationNumber);
    }

    @Override
    public List<LibraryBookAndQuantityDTO> getBooksFilteredByBuilding(Integer id, Integer maxReservationNumber) {
	return booksAndQuantityList(booksDTOList(dao.findByLibraryBuildingId(id)), maxReservationNumber);
    }

    @Override
    public List<LibraryBookAndQuantityDTO> getBooksFilteredByKinds(List<String> kinds, Integer maxReservationNumber) {
	return booksAndQuantityList(booksDTOList(dao.findByKindIn(kinds)), maxReservationNumber);
    }

    @Override
    public List<LibraryBookAndQuantityDTO> getBooksFilteredByLibraryBuildingIdAndKinds(Integer id, List<String> kinds,
	    Integer maxReservationNumber) {
	return booksAndQuantityList(booksDTOList(dao.findByLibraryBuildingIdAndKindIn(id, kinds)),
		maxReservationNumber);
    }

    private List<LibraryBookDTO> booksDTOList(List<LibraryBook> list) {
	return list.stream().map(O -> convertBooksToDTO(O)).collect(Collectors.toList());
    }

    private LibraryBookDTO convertBooksToDTO(LibraryBook book) {
	return modelMapper.map(book, bookDTO.getClass());
    }

    private ArrayList<LibraryBookAndQuantityDTO> booksAndQuantityList(List<LibraryBookDTO> booksDTOList,
	    Integer maxReservationNumber) {
	ArrayList<LibraryBookAndQuantityDTO> booksAndQuantityDTOs = new ArrayList<>();
	booksDTOList.forEach(o -> {
	    if (!isTitleAndLibraryBuildingExist(booksAndQuantityDTOs, o)) {
		booksAndQuantityDTOs.add(bookAndQuantity(booksDTOList, o, maxReservationNumber));
	    }
	});
	return booksAndQuantityDTOs;
    }

    private LibraryBookAndQuantityDTO bookAndQuantity(List<LibraryBookDTO> booksDTOList, LibraryBookDTO bookDTO,
	    Integer maxReservationNumber) {
	LibraryBookAndQuantityDTO book = convertBookDTOToBookAndQuantity(bookDTO);
	book.setNumberOfBookAvailable(booksNumber(booksDTOList, bookDTO, maxReservationNumber));
	return book;
    }

    private Integer booksNumber(List<LibraryBookDTO> booksDTOList, LibraryBookDTO bookDTO,
	    Integer maxReservationNumber) {
	List<LibraryBookDTO> specificBookList = specificBookList(booksDTOList, bookDTO.getTitle(),
		bookDTO.getLibraryBuilding().getName());
	List<LibraryBookDTO> specificBookListNotAvailable = specificBookListNotAvailableBooks(specificBookList);
	Integer size_A = specificBookListSize(specificBookList);
	Integer size_B = specificBookListNotAvailableBooksSize(specificBookListNotAvailable);
	if (size_B == 0) {
	    return size_A;
	} else if (size_A > size_B) {
	    return size_A - size_B;
	} else if (size_B > specificBookListNotReservableBooksSize(
		specificBookListNotReservableBooks(specificBookListNotAvailable, maxReservationNumber))) {
	    return 0;
	} else {
	    return -1;
	}
    }

    private LibraryBookAndQuantityDTO convertBookDTOToBookAndQuantity(LibraryBookDTO bookDTO) {
	return modelMapper.map(bookDTO, booksAndQuantityDTO.getClass());
    }

    private Integer specificBookListNotReservableBooksSize(List<LibraryBookDTO> specificBookListNotReservableBooks) {
	return specificBookListNotReservableBooks.size();
    }

    private List<LibraryBookDTO> specificBookListNotReservableBooks(
	    List<LibraryBookDTO> specificBookListNotAvailableBooks, Integer maxReservationNumber) {
	List<LibraryBookDTO> specificBookListNotReservableBooks = new ArrayList<>();
	specificBookListNotReservableBooks = specificBookListNotAvailableBooks.stream()
		.filter(o -> parseNumberOfreservation(o.getNumberOfReservations()) == maxReservationNumber)
		.collect(Collectors.toList());
	return specificBookListNotReservableBooks;
    }

    /**
     *
     * @param numberOfReservation
     * @return 0 if db number_of_reservation value = null
     */
    private Integer parseNumberOfreservation(Integer numberOfReservation) {
	return (Integer) ObjectUtils.defaultIfNull(numberOfReservation, 0);
    }

    private Integer specificBookListNotAvailableBooksSize(List<LibraryBookDTO> specificBookListNotAvailableBooks) {
	return specificBookListNotAvailableBooks.size();
    }

    private List<LibraryBookDTO> specificBookListNotAvailableBooks(List<LibraryBookDTO> specificBookList) {
	return specificBookList.stream()
		.filter(o -> o.getAvailability() == false || parseNumberOfreservation(o.getNumberOfReservations()) > 0)
		.collect(Collectors.toList());
    }

    private Integer specificBookListSize(List<LibraryBookDTO> specificBookList) {
	return specificBookList.size();
    }

    private List<LibraryBookDTO> specificBookList(List<LibraryBookDTO> booksDTOList, String bookTitle,
	    String building) {
	return booksDTOList.stream()
		.filter(o -> o.getTitle().equals(bookTitle) && o.getLibraryBuilding().getName().equals(building))
		.collect(Collectors.toList());
    }

    private boolean isTitleAndLibraryBuildingExist(List<LibraryBookAndQuantityDTO> booksList, LibraryBookDTO books) {
	for (LibraryBookAndQuantityDTO dto : booksList) {
	    if (isTitleExist(dto, books) && isLibraryBuildingExist(dto, books)) {
		return true;
	    }
	}
	return false;
    }

    private boolean isTitleExist(LibraryBookAndQuantityDTO books, LibraryBookDTO booksDTO) {
	return books.getTitle().equals(booksDTO.getTitle());
    }

    private boolean isLibraryBuildingExist(LibraryBookAndQuantityDTO books, LibraryBookDTO booksDTO) {
	return books.getLibraryBuildingName().equals(booksDTO.getLibraryBuilding().getName());
    }

    @Override
    public List<LibraryBooksKindsDTO> getKindsList() {
	return booksKindsDTOList(dao.findAll());
    }

    private List<LibraryBooksKindsDTO> booksKindsDTOList(List<LibraryBook> list) {
	return list.stream().map(O -> convertBooksToKindsDTO(O)).distinct().collect(Collectors.toList());
    }

    private LibraryBooksKindsDTO convertBooksToKindsDTO(LibraryBook book) {
	return modelMapper.map(book, booksKindsDTO.getClass());
    }

    @Override
    public List<LibraryReservableBookExamplary> getReservableBooks(String title, String buildingName,
	    Integer maxOfReservation) {
	List<LibraryReservableBookExamplary> list = new ArrayList<>();
	list = booksFilterByMaxReservation(title, buildingName, maxOfReservation).stream()
		.map(o -> convertBookToLibraryReservableBook(o)).collect(Collectors.toList());
	return list;
    }

    private List<LibraryBook> booksFilterByMaxReservation(String title, String buildingName, Integer maxOfReservation) {
	return booksByTitleAndBuilding(title, buildingName).stream()
		.filter(o -> o.getNumberOfReservations() < maxOfReservation).collect(Collectors.toList());
    }

    private List<LibraryBook> booksByTitleAndBuilding(String title, String buildingName) {
	return dao.findByTitleAndLibraryBuildingName(title, buildingName).stream()
		.map(o -> parsedNumberOfReservationNullToZero(o)).collect(Collectors.toList());

    }

    private LibraryBook parsedNumberOfReservationNullToZero(LibraryBook book) {
	LibraryBook parsedBook = book;
	parsedBook.setNumberOfReservations(parseNumberOfreservation(book.getNumberOfReservations()));
	return parsedBook;
    }

    private LibraryReservableBookExamplary convertBookToLibraryReservableBook(LibraryBook book) {
	return modelMapper.map(book, reservableBook.getClass());
    }

    @Override
    public LibraryBookDTO getBookById(Integer id) {
	if (dao.findById(id).isPresent()) {
	    LibraryBook optLibraryBook = dao.findById(id).get();
	    LibraryBuilding building = optLibraryBook.getLibraryBuilding();
	    LibraryBookDTO dto = convertBookToDTO(optLibraryBook);
	    LibraryBuildingForBookDTO buildingDTO = convertBuildingToDTO(building);
	    dto.setLibraryBuilding(buildingDTO);
	    return dto;
	}
	throw new BookNotFoundException();
    }

    private LibraryBookDTO convertBookToDTO(LibraryBook entity) {
	return modelMapper.map(entity, bookDTO.getClass());
    }

    private LibraryBuildingForBookDTO convertBuildingToDTO(LibraryBuilding entity) {
	return modelMapper.map(entity, libraryBuildingForBookDTO.getClass());
    }

}

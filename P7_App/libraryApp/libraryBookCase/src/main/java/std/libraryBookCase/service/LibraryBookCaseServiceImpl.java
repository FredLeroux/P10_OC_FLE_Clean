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
import std.libraryBookCase.entities.LibraryBook;

@Service
public class LibraryBookCaseServiceImpl implements LibraryBookCaseService {

    @Autowired
    LibraryBookCaseDAO dao;

    private ModelMapper modelMapper = new ModelMapper();
    private LibraryBookDTO bookDTO = new LibraryBookDTO();
    private LibraryBooksKindsDTO booksKindsDTO = new LibraryBooksKindsDTO();
    private LibraryBookAndQuantityDTO booksAndQuantityDTO = new LibraryBookAndQuantityDTO();

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
	book.setNumber(booksNumber(booksDTOList, bookDTO, maxReservationNumber));
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
	return specificBookList.stream().filter(o -> o.getAvailability() == false).collect(Collectors.toList());
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

    private LibraryBookAndQuantityDTO createBooksAndNumberDTO(LibraryBookDTO book) {
	return new LibraryBookAndQuantityDTO(book.getId(), book.getKind(), book.getTitle(), book.getAuthor(),
		book.getLibraryBuilding().getName(), 1);
    }

    private void incrementBooksAndNumberDTONumber(LibraryBookAndQuantityDTO book) {
	book.setNumber(book.getNumber() + 1);
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

}

package std.libraryBookCase.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import std.libraryBookCase.dao.LibraryBookCaseDAO;
import std.libraryBookCase.dto.BooksAndQuantityDTO;
import std.libraryBookCase.dto.BooksDTO;
import std.libraryBookCase.dto.BooksKindsDTO;
import std.libraryBookCase.entities.Books;

@Service
public class LibraryBookCaseServiceImpl implements LibraryBookCaseService {

    @Autowired
    LibraryBookCaseDAO dao;

    private ModelMapper modelMapper = new ModelMapper();
    private BooksDTO bookDTO = new BooksDTO();
    private BooksKindsDTO booksKindsDTO = new BooksKindsDTO();

    @Override
    public List<BooksAndQuantityDTO> getAllBooks() {
        return booksAndQuantityList(booksDTOList(dao.findByAvailabilityTrue()));
    }

    @Override
	public List<BooksAndQuantityDTO> getBooksFilteredByBuilding(Integer id) {
    	 return booksAndQuantityList(booksDTOList(dao.findByLibraryBuildingIdAndAvailabilityTrue(id)));
	}

    @Override
	public List<BooksAndQuantityDTO> getBooksFilteredByKinds(List<String> kinds) {
    	return booksAndQuantityList(booksDTOList(dao.findByKindInAndAvailabilityTrue(kinds)));
	}

    @Override
	public List<BooksAndQuantityDTO> getBooksFilteredByLibraryBuildingIdAndKinds(Integer id, List<String> kinds) {
    	return booksAndQuantityList(booksDTOList(dao.findByLibraryBuildingIdAndKindInAndAvailabilityTrue(id, kinds)));
	}

    private List<BooksDTO> booksDTOList(List<Books> list) {
        return list.stream().map(O -> convertBooksToDTO(O)).collect(Collectors.toList());
    }

    private BooksDTO convertBooksToDTO(Books book) {
        return modelMapper.map(book, bookDTO.getClass());
    }

    private ArrayList<BooksAndQuantityDTO> booksAndQuantityList(List<BooksDTO> booksDTOList ) {
        ArrayList<BooksAndQuantityDTO> booksAndQuantityDTOs = new ArrayList<>();
        booksDTOList.forEach(o->{
            if(isTitleAndLibraryBuildingExist(booksAndQuantityDTOs, o)) {
                incrementBooksAndNumberDTONumber(booksAndQuantityDTOs.get(booksAndQuantityDTOs.size()-1));
            }else {
            	booksAndQuantityDTOs.add(createBooksAndNumberDTO(o));
            }
        });
        return booksAndQuantityDTOs;
    }


    private boolean isTitleAndLibraryBuildingExist(List<BooksAndQuantityDTO> booksList, BooksDTO books) {
    	for(BooksAndQuantityDTO dto:booksList) {
    		if(isTitleExist(dto, books)&&isLibraryBuildingExist(dto, books)) {
      		  return true;
      	  }
    	}
    	return false;
    }

    private boolean isTitleExist(BooksAndQuantityDTO books, BooksDTO booksDTO) {
    	 return books.getTitle().equals(booksDTO.getTitle());
    }

    private boolean isLibraryBuildingExist(BooksAndQuantityDTO books, BooksDTO booksDTO) {
           return books.getLibraryBuildingName().equals(booksDTO.getLibraryBuilding().getName());
    }

    private BooksAndQuantityDTO createBooksAndNumberDTO(BooksDTO book) {
        return new BooksAndQuantityDTO(book.getId(), book.getKind(), book.getTitle(), book.getAuthor(),
                book.getLibraryBuilding().getName(), 1);
    }

    private void incrementBooksAndNumberDTONumber(BooksAndQuantityDTO book) {
        book.setNumber(book.getNumber() + 1);
    }

	@Override
	public List<BooksKindsDTO> getKindsList() {
		return  booksKindsDTOList(dao.findByAvailabilityTrue());
	}

	 private List<BooksKindsDTO> booksKindsDTOList(List<Books> list) {
	        return list.stream().map(O -> convertBooksToKindsDTO(O)).distinct().collect(Collectors.toList());
	    }
	 private BooksKindsDTO convertBooksToKindsDTO(Books book) {
	        return modelMapper.map(book, booksKindsDTO.getClass());
	    }









}

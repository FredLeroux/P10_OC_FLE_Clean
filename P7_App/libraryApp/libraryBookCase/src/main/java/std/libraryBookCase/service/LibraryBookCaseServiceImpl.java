package std.libraryBookCase.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import std.libraryBookCase.dao.LibraryBookCaseDAO;
import std.libraryBookCase.dto.LibraryBooksAndQuantityDTO;
import std.libraryBookCase.dto.LibraryBooksDTO;
import std.libraryBookCase.dto.LibraryBooksKindsDTO;
import std.libraryBookCase.entities.LibraryBook;

@Service
public class LibraryBookCaseServiceImpl implements LibraryBookCaseService {

    @Autowired
    LibraryBookCaseDAO dao;

    private ModelMapper modelMapper = new ModelMapper();
    private LibraryBooksDTO bookDTO = new LibraryBooksDTO();
    private LibraryBooksKindsDTO booksKindsDTO = new LibraryBooksKindsDTO();

    @Override
    public List<LibraryBooksAndQuantityDTO> getAllBooks() {
        return booksAndQuantityList(booksDTOList(dao.findByAvailabilityTrue()));
    }

    @Override
	public List<LibraryBooksAndQuantityDTO> getBooksFilteredByBuilding(Integer id) {
    	 return booksAndQuantityList(booksDTOList(dao.findByLibraryBuildingIdAndAvailabilityTrue(id)));
	}

    @Override
	public List<LibraryBooksAndQuantityDTO> getBooksFilteredByKinds(List<String> kinds) {
    	return booksAndQuantityList(booksDTOList(dao.findByKindInAndAvailabilityTrue(kinds)));
	}

    @Override
	public List<LibraryBooksAndQuantityDTO> getBooksFilteredByLibraryBuildingIdAndKinds(Integer id, List<String> kinds) {
    	return booksAndQuantityList(booksDTOList(dao.findByLibraryBuildingIdAndKindInAndAvailabilityTrue(id, kinds)));
	}

    private List<LibraryBooksDTO> booksDTOList(List<LibraryBook> list) {
        return list.stream().map(O -> convertBooksToDTO(O)).collect(Collectors.toList());
    }

    private LibraryBooksDTO convertBooksToDTO(LibraryBook book) {
        return modelMapper.map(book, bookDTO.getClass());
    }

    private ArrayList<LibraryBooksAndQuantityDTO> booksAndQuantityList(List<LibraryBooksDTO> booksDTOList ) {
        ArrayList<LibraryBooksAndQuantityDTO> booksAndQuantityDTOs = new ArrayList<>();
        booksDTOList.forEach(o->{
            if(isTitleAndLibraryBuildingExist(booksAndQuantityDTOs, o)) {
                incrementBooksAndNumberDTONumber(booksAndQuantityDTOs.get(booksAndQuantityDTOs.size()-1));
            }else {
            	booksAndQuantityDTOs.add(createBooksAndNumberDTO(o));
            }
        });
        return booksAndQuantityDTOs;
    }


    private boolean isTitleAndLibraryBuildingExist(List<LibraryBooksAndQuantityDTO> booksList, LibraryBooksDTO books) {
    	for(LibraryBooksAndQuantityDTO dto:booksList) {
    		if(isTitleExist(dto, books)&&isLibraryBuildingExist(dto, books)) {
      		  return true;
      	  }
    	}
    	return false;
    }

    private boolean isTitleExist(LibraryBooksAndQuantityDTO books, LibraryBooksDTO booksDTO) {
    	 return books.getTitle().equals(booksDTO.getTitle());
    }

    private boolean isLibraryBuildingExist(LibraryBooksAndQuantityDTO books, LibraryBooksDTO booksDTO) {
           return books.getLibraryBuildingName().equals(booksDTO.getLibraryBuilding().getName());
    }

    private LibraryBooksAndQuantityDTO createBooksAndNumberDTO(LibraryBooksDTO book) {
        return new LibraryBooksAndQuantityDTO(book.getId(), book.getKind(), book.getTitle(), book.getAuthor(),
                book.getLibraryBuilding().getName(), 1);
    }

    private void incrementBooksAndNumberDTONumber(LibraryBooksAndQuantityDTO book) {
        book.setNumber(book.getNumber() + 1);
    }

	@Override
	public List<LibraryBooksKindsDTO> getKindsList() {
		return  booksKindsDTOList(dao.findByAvailabilityTrue());
	}

	 private List<LibraryBooksKindsDTO> booksKindsDTOList(List<LibraryBook> list) {
	        return list.stream().map(O -> convertBooksToKindsDTO(O)).distinct().collect(Collectors.toList());
	    }
	 private LibraryBooksKindsDTO convertBooksToKindsDTO(LibraryBook book) {
	        return modelMapper.map(book, booksKindsDTO.getClass());
	    }









}

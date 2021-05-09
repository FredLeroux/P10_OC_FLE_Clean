package std.libraryStock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import std.libraryBookCase.dao.LibraryBookCaseDAO;
import std.libraryBookCase.entities.LibraryBook;
import std.libraryBookCase.entities.LibraryBuilding;
import std.libraryBookCase.exceptions.BookNotFoundException;
import std.libraryBookCase.service.LibraryBookCaseServiceImpl;

@SpringBootTest(classes = { LibraryBookCaseServiceImpl.class })
@TestPropertySource(locations = "/application-unitTest.properties")
@ExtendWith(MockitoExtension.class)
class LibraryBookCaseApplicationTests {

    @MockBean
    private LibraryBookCaseDAO dao;

    @InjectMocks
    private LibraryBookCaseServiceImpl service = new LibraryBookCaseServiceImpl();

    private LibraryBuilding building;
    private LibraryBook bookA;
    private LibraryBook bookB;
    private LibraryBook bookC;
    private LibraryBook bookD;
    private List<LibraryBook> books;

    @BeforeEach
    public void init() {
	building = new LibraryBuilding(1, "name");
	bookA = new LibraryBook(1, "kind", "title", "author", true, 0, building);
	bookB = new LibraryBook(2, "kind", "title", "author", true, 0, building);
	bookC = new LibraryBook(3, "kind", "title", "author", true, 0, building);
	bookD = new LibraryBook(3, "kind1", "title1", "author", true, 0, new LibraryBuilding(2, "name1"));
	books = new ArrayList<LibraryBook>();
	books.add(bookA);
	books.add(bookB);
	books.add(bookC);
    }

    @Test
    public void getBooksTests() {
	List<String> kinds = new ArrayList<String>();
	kinds.add("1");
	kinds.add("2");
	when(dao.findAll()).thenReturn(books);
	when(dao.findByLibraryBuildingId(ArgumentMatchers.any())).thenReturn(books);
	when(dao.findByKindIn(ArgumentMatchers.any())).thenReturn(books);
	when(dao.findByLibraryBuildingIdAndKindIn(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(books);
	assertThat(service.getAllBooks(2).get(0).getNumberOfBookAvailable()).isEqualTo(3);
	assertThat(service.getBooksFilteredByBuilding(1, 2).get(0).getNumberOfBookAvailable()).isEqualTo(3);
	assertThat(service.getBooksFilteredByKinds(kinds, 2).get(0).getNumberOfBookAvailable()).isEqualTo(3);
	assertThat(service.getBooksFilteredByLibraryBuildingIdAndKinds(1, kinds, 2).get(0).getNumberOfBookAvailable())
		.isEqualTo(3);
	bookB.setNumberOfReservations(1);
	assertThat(service.getAllBooks(2).get(0).getNumberOfBookAvailable()).isEqualTo(2);
	assertThat(service.getBooksFilteredByBuilding(1, 2).get(0).getNumberOfBookAvailable()).isEqualTo(2);
	assertThat(service.getBooksFilteredByKinds(kinds, 2).get(0).getNumberOfBookAvailable()).isEqualTo(2);
	assertThat(service.getBooksFilteredByLibraryBuildingIdAndKinds(1, kinds, 2).get(0).getNumberOfBookAvailable())
		.isEqualTo(2);
	bookA.setAvailability(false);
	bookC.setAvailability(false);
	assertThat(service.getAllBooks(2).get(0).getNumberOfBookAvailable()).isEqualTo(0);
	assertThat(service.getBooksFilteredByBuilding(1, 2).get(0).getNumberOfBookAvailable()).isEqualTo(0);
	assertThat(service.getBooksFilteredByKinds(kinds, 2).get(0).getNumberOfBookAvailable()).isEqualTo(0);
	assertThat(service.getBooksFilteredByLibraryBuildingIdAndKinds(1, kinds, 2).get(0).getNumberOfBookAvailable())
		.isEqualTo(0);
	bookA.setNumberOfReservations(2);
	bookB.setNumberOfReservations(2);
	bookC.setNumberOfReservations(2);
	assertThat(service.getAllBooks(2).get(0).getNumberOfBookAvailable()).isEqualTo(-1);
	assertThat(service.getBooksFilteredByBuilding(1, 2).get(0).getNumberOfBookAvailable()).isEqualTo(-1);
	assertThat(service.getBooksFilteredByKinds(kinds, 2).get(0).getNumberOfBookAvailable()).isEqualTo(-1);
	assertThat(service.getBooksFilteredByLibraryBuildingIdAndKinds(1, kinds, 2).get(0).getNumberOfBookAvailable())
		.isEqualTo(-1);
    }

    @Test
    public void getAllBooksTestMultiBook() {
	books.add(bookD);
	when(dao.findAll()).thenReturn(books);
	assertThat(service.getAllBooks(2).size()).isEqualTo(2);
    }

    @Test
    public void getKindsListTest() {
	books.add(bookD);
	when(dao.findAll()).thenReturn(books);
	assertThat(service.getKindsList().size()).isEqualTo(2);
	service.getKindsList().forEach(
		o -> assertThat(o.getKind().equals(bookA.getKind()) || o.getKind().equals(bookD.getKind())).isTrue());
    }

    @Test
    public void getReservableBooksTest() {
	when(dao.findByTitleAndLibraryBuildingName(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(books);
	assertThatCode(() -> service.getReservableBooks("title", "buildingName", 2)).doesNotThrowAnyException();
    }

    @Test
    public void getBookByIdTest() {
	when(dao.findById(ArgumentMatchers.any())).thenReturn(Optional.of(bookA));
	assertThatCode(() -> service.getBookById(1)).doesNotThrowAnyException();
    }

    @Test
    public void getBookByIdTestFail() {
	when(dao.findById(ArgumentMatchers.any())).thenReturn(Optional.empty());
	assertThatThrownBy(() -> service.getBookById(1)).isInstanceOf(BookNotFoundException.class);
    }
}

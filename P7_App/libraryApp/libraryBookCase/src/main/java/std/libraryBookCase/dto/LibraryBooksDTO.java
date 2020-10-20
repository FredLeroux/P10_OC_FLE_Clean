package std.libraryBookCase.dto;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import std.libraryBookCase.entities.LibraryBuilding;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LibraryBooksDTO {



	private Integer id;
	private String kind;
	private String title;
	private String author;
	private LibraryBuilding libraryBuilding;


}

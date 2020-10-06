package std.libraryBookCase.dto;

import com.fasterxml.jackson.annotation.JsonFilter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import std.libraryBookCase.entities.LibraryBuilding;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class BooksAndQuantityDTO {

	private Integer id;
	private String kind;
	private String title;
	private String author;
	private String libraryBuildingName;
	private Integer number;

}

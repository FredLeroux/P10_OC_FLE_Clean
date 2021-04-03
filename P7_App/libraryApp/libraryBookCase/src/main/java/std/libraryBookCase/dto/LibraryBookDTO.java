package std.libraryBookCase.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import std.libraryBookCase.entities.LibraryBuilding;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class LibraryBookDTO {

    private Integer id;
    private String kind;
    private String title;
    private String author;
    private Boolean availability;
    private Integer numberOfReservations;
    private LibraryBuilding libraryBuilding;

}

package std.libraryReservations.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LibraryBookForReservationDTO {

    private Integer id;
    private String kind;
    private String title;
    private String author;
    private Boolean availability;
    private LibraryBuildingForReservationDTO libraryBuilding;
}

package std.libraryBookLoans.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReservableBookExamplaryDatedDTO {

    private Integer reference;
    private String closestReturnDate;
    private String farrestReturnDate;

}

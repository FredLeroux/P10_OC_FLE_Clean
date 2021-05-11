package std.libraryUi.beans;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReservableBookExamplaryDatedBean {

    private Integer reference;
    private String closestReturnDate;
    private String farrestReturnDate;
}

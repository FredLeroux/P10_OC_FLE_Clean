package std.libraryBookLoans.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import std.libraryBookLoans.entities.LibraryBookLoan;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReservableBookLinkedLoanDTO {

    private String returnDate;
    private Boolean postponed;
    private LibraryBookLoan book;
}

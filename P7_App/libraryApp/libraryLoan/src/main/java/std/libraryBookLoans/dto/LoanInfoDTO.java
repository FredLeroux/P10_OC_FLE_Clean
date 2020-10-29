package std.libraryBookLoans.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoanInfoDTO {

	private Integer id;
	private String returnDate;
	private Boolean postponed;
	private LibraryBookLoanInfoDTO book;

}

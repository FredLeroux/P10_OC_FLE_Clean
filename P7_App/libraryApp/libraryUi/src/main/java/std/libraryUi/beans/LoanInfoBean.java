package std.libraryUi.beans;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoanInfoBean {

	private Integer id;
	private String returnDate;
	private Boolean postponed;
	private LibraryBookLoanInfoBean book;

}

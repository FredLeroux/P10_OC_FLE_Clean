package std.libraryBookLoans.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateLoanDTO {


	private Integer bookId;
	private Integer custommerId;
}

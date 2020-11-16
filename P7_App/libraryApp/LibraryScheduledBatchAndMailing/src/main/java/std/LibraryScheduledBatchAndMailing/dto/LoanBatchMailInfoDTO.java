package std.LibraryScheduledBatchAndMailing.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoanBatchMailInfoDTO {

	private String returnDate;
	private LibraryBookBatchTitleDTO book;
	private CustomerBatchEmailDTO customer;

}

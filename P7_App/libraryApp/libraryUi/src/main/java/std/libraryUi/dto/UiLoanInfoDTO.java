package std.libraryUi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UiLoanInfoDTO {

	private Integer id;
	private Integer daysLeft;
	private String localizedFullDate;
	private String bookTitle;
	private Boolean postponed;

}

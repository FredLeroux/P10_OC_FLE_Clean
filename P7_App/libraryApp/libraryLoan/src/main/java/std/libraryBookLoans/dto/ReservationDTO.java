package std.libraryBookLoans.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReservationDTO {

    private Integer id;
    private String notificationDate;
    private Boolean canceledStatus;
    private Integer priority;
    private String BuildingName;
    private LibraryBookLoanInfoDTO book;
    private CustomerLoanDTO customer;
}

package std.libraryReservations.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateReservationDTO {

    private String notificationDate;
    private Boolean canceledStatus;
    private Integer priority;
    private LibraryBookForReservationDTO book;
    private LibraryCustomerForReservationDTO customer;

}

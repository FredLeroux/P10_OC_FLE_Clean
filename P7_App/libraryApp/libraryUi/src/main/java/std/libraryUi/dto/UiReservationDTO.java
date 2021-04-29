package std.libraryUi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UiReservationDTO {

    private Integer reference;
    private String title;
    private Integer bookId;
    private String building;
    private String notificationDate;
    private String returnDate;
    private Boolean postpone;
    private Integer priority;

}

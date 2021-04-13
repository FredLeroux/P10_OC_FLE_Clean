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
    private String building;
    private String notificationDate;

}

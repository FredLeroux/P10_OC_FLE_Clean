package std.libraryUi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UiNotificationReservationDTO {

    private String customerEmail;
    private String bookTitle;
    private String building;

}

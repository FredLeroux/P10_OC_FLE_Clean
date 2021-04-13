package std.libraryUi.beans;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LibraryReservationBean {

    private Integer id;
    private String notificationDate;
    private Boolean canceledStatus;
    private Integer priority;
    private String BuildingName;
    private BooksBean book;
    private LibraryCustomerBean customer;
}

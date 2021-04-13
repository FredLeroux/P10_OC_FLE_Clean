package std.libraryReservations.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import std.libraryReservations.entities.LibraryBookForReservation;
import std.libraryReservations.entities.LibraryCustomerForReservation;

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
    private LibraryBookForReservation book;
    private LibraryCustomerForReservation customer;
}

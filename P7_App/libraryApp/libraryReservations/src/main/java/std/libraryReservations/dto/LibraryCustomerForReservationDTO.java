package std.libraryReservations.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LibraryCustomerForReservationDTO {

    private Integer id;
    private String customerEmail;
    private String customerPassword;
    private Boolean customerAccountNonExpired;
    private Boolean customerCredentialNonExpired;
    private Boolean customerAccountNonLocked;
    private String customerAuthToken;
    private LibraryRoleForReservationDTO role;
}

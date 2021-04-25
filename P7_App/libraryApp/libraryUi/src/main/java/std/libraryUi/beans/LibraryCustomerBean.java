package std.libraryUi.beans;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LibraryCustomerBean {

    private Integer id;
    private String customerEmail;
    private String customerPassword;
    private Boolean customerEnabled;
    private Boolean customerAccountNonExpired;
    private Boolean customerCredentialNonExpired;
    private Boolean customerAccountNonLocked;
    private LibraryRoleBean roles;
}

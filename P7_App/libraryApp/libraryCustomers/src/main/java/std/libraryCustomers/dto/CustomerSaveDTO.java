package std.libraryCustomers.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import std.libraryCustomers.entities.LibraryRole;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomerSaveDTO {

	private Integer id;
	private String customerEmail;
	private String customerPassword;
	private Boolean customerEnabled;
	private Boolean customerAccountNonExpired;
	private Boolean customerCredentialNonExpired;
	private Boolean customerAccountNonLocked;
	private LibraryRole roles;

}

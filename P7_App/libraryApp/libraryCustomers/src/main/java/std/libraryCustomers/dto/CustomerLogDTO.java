package std.libraryCustomers.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import std.libraryCustomers.entities.LibraryRole;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomerLogDTO {

	private String customerEmail;
	private String customerPassword;
	private LibraryRoleDTO role;

}

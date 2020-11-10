package std.libraryAPIGatewayZuul.security.beans;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LibraryCustomerLogBean {


	private String customerEmail;
	private String customerPassword;
	private LibraryRoleBean role;

}

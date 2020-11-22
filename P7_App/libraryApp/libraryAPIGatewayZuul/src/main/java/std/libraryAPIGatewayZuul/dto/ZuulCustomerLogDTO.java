package std.libraryAPIGatewayZuul.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ZuulCustomerLogDTO {


	private String customerEmail;
	private String customerPassword;
	private String customerAuthToken;
	private ZuulLibraryRoleDTO role;

}

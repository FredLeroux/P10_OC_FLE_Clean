package std.libraryCustomers.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomerDTO {

	private Integer id;
	private String customerEmail;
	private String customerPassword;

}

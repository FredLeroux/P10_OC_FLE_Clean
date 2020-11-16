package std.libraryBookLoans.dto;

import javax.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomerLoanDTO {

	private Integer id;
	private String customerEmail;
	private String customerPassword;
	private Boolean customerEnabled;
	private Boolean customerAccountNonExpired;
	private Boolean customerCredentialNonExpired;
	private Boolean customerAccountNonLocked;
	private String customerAuthToken;
	private LibraryRoleLoanDTO role;

}

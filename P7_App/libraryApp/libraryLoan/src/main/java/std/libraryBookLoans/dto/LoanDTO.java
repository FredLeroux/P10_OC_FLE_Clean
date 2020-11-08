package std.libraryBookLoans.dto;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import std.libraryBookLoans.entities.CustomerLoan;
import std.libraryBookLoans.entities.LibraryBookLoan;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoanDTO {

	private String returnDate;
	private Boolean postponed;
	private Boolean returned;
	private LibraryBookLoan book;
	private CustomerLoan customer;
}

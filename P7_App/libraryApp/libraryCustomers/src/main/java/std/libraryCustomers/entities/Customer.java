package std.libraryCustomers.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "customer", schema ="libraryum")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name = "customer_email")
	private String customerEmail;
	@Column(name = "customer_password")
	private String customerPassword;


}

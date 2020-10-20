package std.libraryCustomers.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "library_customers", schema ="libraryum")
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
	@Column(name = "customer_enabled")
	private Boolean customerEnabled;
	@Column(name = "customer_account_non_expired")
	private Boolean customerAccountNonExpired;
	@Column(name = "customer_credentials_non_expired")
	private Boolean customerCredentialNonExpired;
	@Column(name = "customer_account_non_locked")
	private Boolean customerAccountNonLocked;
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "library_role_fk")
	private LibraryRole role;


}

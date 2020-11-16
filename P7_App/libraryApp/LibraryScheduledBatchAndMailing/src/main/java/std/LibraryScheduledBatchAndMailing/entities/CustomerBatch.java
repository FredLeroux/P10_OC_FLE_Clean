package std.LibraryScheduledBatchAndMailing.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "library_customers", schema ="libraryum")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomerBatch {
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
	@Column(name ="customer_auth_token")
	private String customerAuthToken;
	@ManyToOne(cascade = CascadeType.PERSIST)
	@OnDelete(action = OnDeleteAction.NO_ACTION)
	@JoinColumn(name = "library_role_fk")
	private LibraryRoleBatch role;


}

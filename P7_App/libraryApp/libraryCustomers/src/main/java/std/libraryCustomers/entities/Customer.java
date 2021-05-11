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
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "library_customers", schema = "libraryum")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    @Column(name = "customer_email")
    private String customerEmail;
    @NotNull
    @Column(name = "customer_password")
    private String customerPassword;
    @NotNull
    @Column(name = "customer_enabled")
    private Boolean customerEnabled;
    @NotNull
    @Column(name = "customer_account_non_expired")
    private Boolean customerAccountNonExpired;
    @NotNull
    @Column(name = "customer_credentials_non_expired")
    private Boolean customerCredentialNonExpired;
    @NotNull
    @Column(name = "customer_account_non_locked")
    private Boolean customerAccountNonLocked;
    @Column(name = "customer_auth_token")
    private String customerAuthToken;
    @NotNull
    @ManyToOne(cascade = CascadeType.PERSIST)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @JoinColumn(name = "library_role_fk")
    private LibraryRole role;

}

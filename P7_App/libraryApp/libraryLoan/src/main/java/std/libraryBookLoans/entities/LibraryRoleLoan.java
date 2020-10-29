package std.libraryBookLoans.entities;

import java.io.Serializable;

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
@Table(name = "library_roles", schema = "libraryum")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LibraryRoleLoan implements Serializable {

	private static final long serialVersionUID = -9068201568653054991L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name ="role_type")
	private String roleType;

}

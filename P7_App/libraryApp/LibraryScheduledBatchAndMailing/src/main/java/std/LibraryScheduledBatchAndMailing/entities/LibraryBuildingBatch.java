package std.LibraryScheduledBatchAndMailing.entities;

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
@Table(name ="library_building",schema = "libraryum")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LibraryBuildingBatch implements Serializable {


	private static final long serialVersionUID = 4784353407456821636L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "name")
	private String name;

}

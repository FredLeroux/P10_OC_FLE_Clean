package std.LibraryScheduledBatchAndMailing.entities;

import java.io.Serializable;

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
@Table(name = "library_books", schema = "libraryum")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LibraryBookBatch implements Serializable {


	private static final long serialVersionUID = 7123011051900300385L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "kind")
	private String kind;

	@Column(name = "title")
	private String title;

	@Column(name = "author")
	private String author;

	@Column(name = "availability")
	private Boolean availability;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@OnDelete(action = OnDeleteAction.NO_ACTION)
	@JoinColumn(name = "library_building_fk")
	private LibraryBuildingBatch libraryBuilding;

}

package std.libraryUi.beans;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BooksBean {

    private Integer id;
    private String kind;
    private String title;
    private String author;
    private String libraryBuildingName;
    private Integer numberOfBookAvailable;

}

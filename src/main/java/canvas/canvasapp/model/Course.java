package canvas.canvasapp.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.experimental.Accessors;


@Entity
@Data
@Accessors(chain = true)
public class Course {

	@Id
	@Column(nullable = false)
	private Long id;

	@Column(unique = true, nullable = false)
	private String name;
	@Column(unique = true, nullable = false)
	private String courseCode;
	private Boolean selected;

}

package canvas.canvasapp.model;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;


@Entity
@Data
@Accessors(chain = true)
public class Course {

	@Id
	private Long id;

	@Column(unique = true
	)
	private String name;
	private String courseCode;
	private Boolean selected;

}

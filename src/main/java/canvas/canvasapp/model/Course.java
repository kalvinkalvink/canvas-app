package canvas.canvasapp.model;


import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.experimental.Accessors;


@Entity
@Data
@Accessors(chain = true)
public class Course {
	public enum Selected{
		Y,N
	}
	@Id
	private Long id;
	private String name;
	private String courseCode;
	@Enumerated(EnumType.STRING)
	private Selected selected;

}

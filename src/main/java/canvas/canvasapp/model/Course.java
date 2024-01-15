package canvas.canvasapp.model;

import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.UUID;

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

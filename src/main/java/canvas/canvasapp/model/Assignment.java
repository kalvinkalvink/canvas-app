package canvas.canvasapp.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Entity
@Data
@Accessors(chain = true)
public class Assignment {
	@Id
	@Column(nullable = false)
	private Long id;
	@Column(nullable = false)
	private String name;
	private Date createdAt;
	private Date updatedAt;
	private Date dueAt;
	private Date lockAt;
	private Date unlockAt;
//	@Column(name = "course_id", insertable = false, updatable = false)
//	private String courseId;

	@ManyToOne(cascade = CascadeType.ALL)
	private Course course;
}

package canvas.canvasapp.model.db;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Date;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Accessors(chain = true)
public class GradebookHistory {
	@Id
	private Long id;
	@ManyToOne(cascade = CascadeType.ALL)
	private Assignment assignment;
	private String assignment_name;
	private String body;
	private String current_grade;
	private Date current_graded_at;
	private String current_grader;
	private Date graded_at;
	private String grader;
	private Long grader_id;
	private String new_grade;
	private Date new_graded_at;
	private String new_grader;
	private String previous_grade;
	private Date previous_graded_at;
	private String previous_grader;
	private Long score;
	private String user_name;
	private String submission_type;
	private String url;
	private Long user_id;
}

package canvas.canvasapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
	@Column(length = 4192)
    private String description;
    private Date createdAt;
    private Date updatedAt;
    private Date dueAt;
    private Date lockAt;
    private Date unlockAt;
    private String courseId;
}

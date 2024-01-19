package canvas.canvasapp.model;

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
    private Long id;
    private String name;
    private String description;
    private Date createdAt;
    private Date updatedAt;
    private Date dueAt;
    private Date lockAt;
    private Date unlockAt;
    private String courseId;
}

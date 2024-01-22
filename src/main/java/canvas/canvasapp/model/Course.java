package canvas.canvasapp.model;


import canvas.canvasapp.model.converter.ColorConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import javafx.scene.paint.Color;
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
	@Column(nullable = false)
	private Boolean selected;

	@Convert(converter = ColorConverter.class)
	private Color color;

}

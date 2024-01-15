package canvas.canvasapp.model.setting;

import canvas.canvasapp.model.Course;
import javafx.beans.property.BooleanProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SelectedCourseSetting {
	private Course course;
	private BooleanProperty selectedBooleanProperty;
}

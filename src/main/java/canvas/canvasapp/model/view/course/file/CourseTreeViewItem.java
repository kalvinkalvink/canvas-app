package canvas.canvasapp.model.view.course.file;

import canvas.canvasapp.model.db.File;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CourseTreeViewItem {
	private String name;
	private File canvasFile;
}

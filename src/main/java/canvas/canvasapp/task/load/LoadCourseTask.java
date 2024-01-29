package canvas.canvasapp.task.load;

import canvas.canvasapp.model.Course;
import canvas.canvasapp.service.database.CourseService;
import javafx.concurrent.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class LoadCourseTask extends Task<List<Course>> {
	@Autowired
	CourseService courseService;

	@Override
	protected List<Course> call() throws Exception {
		return courseService.findAll();
	}
}

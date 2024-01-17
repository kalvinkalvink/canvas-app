package canvas.canvasapp.task;

import canvas.canvasapp.dao.CourseRepository;
import canvas.canvasapp.util.CanvasApi;
import edu.ksu.canvas.interfaces.CourseReader;
import edu.ksu.canvas.model.Course;
import edu.ksu.canvas.requestOptions.ListCurrentUserCoursesOptions;
import javafx.concurrent.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Scope("prototype")
public class StartupFetchDataTask extends Task<Void> {
	@Autowired
	CourseRepository courseRepository;
	@Autowired
	CanvasApi canvasApi;

	@Override
	protected Void call() throws Exception {
		try {
			CourseReader courseReader = canvasApi.getReader(CourseReader.class);
			List<Course> canvasCourseList = courseReader.listCurrentUserCourses(new ListCurrentUserCoursesOptions());
			Iterable<canvas.canvasapp.model.Course> dbCourseIt = courseRepository.findAll();
			List<canvas.canvasapp.model.Course> dbCourseList = new ArrayList<>();
			dbCourseIt.forEach(dbCourseList::add);
			canvasCourseList.stream().filter(course -> dbCourseList.stream().noneMatch(dbCourse -> dbCourse.getId().equals(course.getId())))
					.filter(canvasCourse -> canvasCourse.getName() != null && canvasCourse.getCourseCode() != null)
					.forEach(canvasCourse -> {
						System.out.println(canvasCourse.getName());
						canvas.canvasapp.model.Course course = new canvas.canvasapp.model.Course()
								.setId(canvasCourse.getId())
								.setName(canvasCourse.getName())
								.setCourseCode(canvasCourse.getCourseCode())
								.setSelected(canvas.canvasapp.model.Course.Selected.N);
						courseRepository.save(course);
					});
		} catch (IOException e) {
			log.error("Failed to fetch course data", e);
		}
		return null;
	}
}

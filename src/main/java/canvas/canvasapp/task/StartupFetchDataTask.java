package canvas.canvasapp.task;

import canvas.canvasapp.dao.CourseDao;
import canvas.canvasapp.util.CanvasApi;
import edu.ksu.canvas.interfaces.CourseReader;
import edu.ksu.canvas.model.Course;
import edu.ksu.canvas.requestOptions.ListCurrentUserCoursesOptions;
import javafx.concurrent.Task;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

@Slf4j
public class StartupFetchDataTask extends Task<Void> {

	private final CourseDao courseDao;
	private CanvasApi canvasApi = CanvasApi.getInstance();

	public StartupFetchDataTask() {
		courseDao = new CourseDao();
	}

	@Override
	protected Void call() throws Exception {
		try {
			CourseReader courseReader = canvasApi.getReader(CourseReader.class);
			List<Course> canvasCourseList = courseReader.listCurrentUserCourses(new ListCurrentUserCoursesOptions());
			List<canvas.canvasapp.model.Course> dbCourseList = courseDao.getAll();
			canvasCourseList.stream().filter(course -> dbCourseList.stream().noneMatch(dbCourse -> dbCourse.getId().equals(course.getId())))
					.filter(canvasCourse -> canvasCourse.getName() != null && canvasCourse.getCourseCode() != null)
					.forEach(canvasCourse -> {
						System.out.println(canvasCourse.getName());
						canvas.canvasapp.model.Course course = new canvas.canvasapp.model.Course()
								.setId(canvasCourse.getId())
								.setName(canvasCourse.getName())
								.setCourseCode(canvasCourse.getCourseCode())
								.setSelected(canvas.canvasapp.model.Course.Selected.N);
						courseDao.saveOrUpdate(course);
					});
		} catch (IOException e) {
			log.error("Failed to fetch course data", e);
		}
		return null;
	}
}

package canvas.canvasapp.task;

import canvas.canvasapp.controller.view.PreferenceController;
import canvas.canvasapp.dao.CourseRepository;
import canvas.canvasapp.util.CanvasApi;
import edu.ksu.canvas.interfaces.AssignmentReader;
import edu.ksu.canvas.interfaces.CourseReader;
import edu.ksu.canvas.model.Course;
import edu.ksu.canvas.requestOptions.ListCourseAssignmentsOptions;
import edu.ksu.canvas.requestOptions.ListCurrentUserCoursesOptions;
import javafx.beans.property.ListProperty;
import javafx.concurrent.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Scope("prototype")
public class StartupFetchDataTask extends Task<Void> {
	@Autowired
	CourseRepository courseRepository;
	@Autowired
	CanvasApi canvasApi;
	@Autowired
	PreferenceController preferenceController;

	@Override
	protected Void call() throws Exception {
		fetchCourse();
//		fetchAssignment();
		return null;
	}
	private void fetchCourse(){
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
								.setSelected(false);
						courseRepository.save(course);
					});
		} catch (IOException e) {
			log.error("Failed to fetch course data", e);
		}

	}
	private void fetchAssignment(){
		// read selected course
		ListProperty<String> courseSelections = preferenceController.getCourseSelections();
		List<canvas.canvasapp.model.Course> selectedCourseList = courseSelections.stream()
				.map(courseRepository::findByNameAndSelectedIsTrue)
				.collect(Collectors.toList());

		// fetch course assignments
		AssignmentReader assignmentReader = canvasApi.getReader(AssignmentReader.class);
		selectedCourseList.stream().map((course)->{
			try {
				assignmentReader.listCourseAssignments(new ListCourseAssignmentsOptions(course.getId().toString()));
			} catch (IOException e) {
//				log.warn();
			}
			return null;
		}).collect(Collectors.toList());


	}
}

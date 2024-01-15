package canvas.canvasapp.task;

import canvas.canvasapp.dao.CourseDao;
import canvas.canvasapp.model.Course;
import canvas.canvasapp.util.CanvasApi;
import edu.ksu.canvas.interfaces.AssignmentReader;
import edu.ksu.canvas.model.assignment.Assignment;
import edu.ksu.canvas.requestOptions.ListCourseAssignmentsOptions;
import javafx.concurrent.Task;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
public class FetchAssignmentsTask extends Task<Map<Date, List<Assignment>>> {
	private CanvasApi canvasApi = CanvasApi.getInstance();
	private CourseDao courseDao = new CourseDao();

	@Override
	protected Map<Date, List<Assignment>> call() throws Exception {
		List<Course> courseList = courseDao.getAll();
		AssignmentReader assignmentReader = canvasApi.getReader(AssignmentReader.class);
		ArrayList<Assignment> assignmentArrayList = new ArrayList<>();
		// get all selected course
//		courseList.stream().filter(course -> course.getSelected().toString() == "Y").forEach(course -> {
//			try {
//				List<Assignment> assignments = assignmentReader.listCourseAssignments(new ListCourseAssignmentsOptions(course.getId().toString()));
//				assignmentArrayList.addAll(assignments);
//
//			} catch (IOException e) {
//				log.error("Failed to fetch course assignment", course.getId(), e);
//			}
//		});
		System.out.println(assignmentArrayList);
//		Collections.sort(assignmentArrayList, Comparator.comparing(Assignment::getDueAt));
//		Map<Date, List<Assignment>> assignmentByDueAt = assignmentArrayList.stream().collect(Collectors.groupingBy(Assignment::getDueAt));
//		System.out.println(assignmentByDueAt);
		return null;
	}
}

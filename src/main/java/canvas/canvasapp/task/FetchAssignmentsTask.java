package canvas.canvasapp.task;

import canvas.canvasapp.dao.CourseRepository;
import canvas.canvasapp.model.Course;
import canvas.canvasapp.util.CanvasApi;
import edu.ksu.canvas.interfaces.AssignmentReader;
import edu.ksu.canvas.model.assignment.Assignment;
import edu.ksu.canvas.requestOptions.ListCourseAssignmentsOptions;
import javafx.concurrent.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Scope("prototype")
public class FetchAssignmentsTask extends Task<Map<Date, List<Assignment>>> {
	@Autowired
	CanvasApi canvasApi;
	@Autowired
	CourseRepository courseRepository;

	@Override
	protected Map<Date, List<Assignment>> call() throws Exception {
		System.out.println("running fetch assignment task");
		Iterable<Course> courseIt = courseRepository.findAll();
		ArrayList<Course> courseList = new ArrayList<>();
		courseIt.forEach(courseList::add);
		AssignmentReader assignmentReader = canvasApi.getReader(AssignmentReader.class);
		ArrayList<Assignment> assignmentArrayList = new ArrayList<>();
		// get all selected course
		courseList.stream().filter(course -> course.getSelected().toString() == "Y").forEach(course -> {
			try {
				List<Assignment> assignments = assignmentReader.listCourseAssignments(new ListCourseAssignmentsOptions(course.getId().toString()));
				assignmentArrayList.addAll(assignments);

			} catch (IOException e) {
				log.error("Failed to fetch course assignment", course.getId(), e);
			}
		});

		Collections.sort(assignmentArrayList, Comparator.comparing(Assignment::getDueAt));
		Map<Date, List<Assignment>> assignmentByDueAt = assignmentArrayList.stream().collect(Collectors.groupingBy(Assignment::getDueAt));
		System.out.println(assignmentByDueAt);
		return assignmentByDueAt;
	}
}

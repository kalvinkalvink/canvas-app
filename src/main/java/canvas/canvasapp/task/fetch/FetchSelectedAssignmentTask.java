package canvas.canvasapp.task.fetch;

import canvas.canvasapp.controller.view.PreferenceController;
import canvas.canvasapp.dao.AssignmentRepository;
import canvas.canvasapp.dao.CourseRepository;
import canvas.canvasapp.model.Course;
import canvas.canvasapp.util.CanvasApi;
import edu.ksu.canvas.interfaces.AssignmentReader;
import edu.ksu.canvas.model.assignment.Assignment;
import edu.ksu.canvas.requestOptions.ListCourseAssignmentsOptions;
import javafx.beans.property.ListProperty;
import javafx.concurrent.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Scope("prototype")
public class FetchSelectedAssignmentTask extends Task<Void> {
	@Autowired
	CanvasApi canvasApi;
	@Autowired
	AssignmentRepository assignmentRepository;
	@Autowired
	CourseRepository courseRepository;
	@Autowired
	PreferenceController preferenceController;

	@Override
	protected Void call() throws Exception {
		log.info("Fetching selected course assignments");
		log.debug("Assignment table size: {}", assignmentRepository.count());
		AssignmentReader assignmentReader = canvasApi.getReader(AssignmentReader.class);
		// read selected course
		List<Course> selectedCourseList = courseRepository.getBySelectedIsTrue();
		log.debug("Selected course list size: {}", selectedCourseList.size());
 		List<canvas.canvasapp.model.Assignment> assignmentList = new ArrayList<>();
		selectedCourseList.stream()
				.map(selectedCourse -> {
					try {
						return assignmentReader.listCourseAssignments(new ListCourseAssignmentsOptions(selectedCourse.getId().toString()));
					} catch (IOException e) {
						log.warn("Failed to fetch {} assignments", selectedCourse.getName());
					}
					return new ArrayList<Assignment>();
				})
				.flatMap(List::stream)
				.map(canvasAssignment -> new canvas.canvasapp.model.Assignment()
						.setId(canvasAssignment.getId())
						.setName(canvasAssignment.getName())
						.setDescription(canvasAssignment.getDescription())
						.setCreatedAt(canvasAssignment.getCreatedAt())
						.setUpdatedAt(canvasAssignment.getUpdatedAt())
						.setDueAt(canvasAssignment.getDueAt())
						.setLockAt(canvasAssignment.getLockAt())
						.setUnlockAt(canvasAssignment.getUnlockAt())
						.setCourseId(canvasAssignment.getCourseId()))
				.forEach(assignmentList::add);
		assignmentRepository.saveAll(assignmentList);
		return null;
	}
}

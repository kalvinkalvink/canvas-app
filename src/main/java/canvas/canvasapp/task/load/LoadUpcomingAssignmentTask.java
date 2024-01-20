package canvas.canvasapp.task.load;

import canvas.canvasapp.dao.AssignmentRepository;
import canvas.canvasapp.model.Assignment;
import javafx.concurrent.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Scope("prototype")
public class LoadUpcomingAssignmentTask extends Task<Map<Date, List<Assignment>>> {
	@Autowired
	AssignmentRepository assignmentRepository;


	@Override
	protected Map<Date, List<Assignment>> call() throws Exception {
		List<Assignment> assignmentList = assignmentRepository.findAll();
		return assignmentList.stream()
				.filter(assignment -> Objects.nonNull(assignment.getDueAt()))
				.filter(assignment -> !assignment.getDueAt().before(new Date()))
				.sorted(Comparator.comparing(Assignment::getDueAt).reversed())
				.collect(Collectors.groupingBy(Assignment::getDueAt));
	}
}

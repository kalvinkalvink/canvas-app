package canvas.canvasapp.task.load;

import canvas.canvasapp.dao.AssignmentRepository;
import canvas.canvasapp.model.Assignment;
import javafx.concurrent.Task;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Scope("prototype")
public class LoadUpcomingAssignmentTask extends Task<TreeMap<Date, List<Assignment>>> {
	@Autowired
	AssignmentRepository assignmentRepository;


	@Override
	protected TreeMap<Date, List<Assignment>> call() {
		List<Assignment> assignmentList = assignmentRepository.findAll();
		return assignmentList.stream()
				.filter(assignment -> Objects.nonNull(assignment.getDueAt()))
				.filter(assignment -> !assignment.getDueAt().before(new Date()))
//				.sorted(Comparator.comparing(Assignment::getDueAt).reversed())
				.collect(Collectors.toMap(Assignment::getDueAt,
						assignment -> Collections.singletonList(assignment),
						(list1, list2) -> {
							List<Assignment> mergedList = new ArrayList<>(list1);
							mergedList.addAll(list2);
							return mergedList;
						},
						TreeMap::new
				));
	}
}

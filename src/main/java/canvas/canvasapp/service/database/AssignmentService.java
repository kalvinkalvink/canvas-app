package canvas.canvasapp.service.database;

import canvas.canvasapp.event.publisher.database.DatabaseUpdatedEventPublisher;
import canvas.canvasapp.model.Assignment;
import canvas.canvasapp.repository.AssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AssignmentService implements IDatabaseUpdateEvent {
	@Autowired
	AssignmentRepository assignmentRepository;
	@Autowired
	DatabaseUpdatedEventPublisher databaseUpdatedEventPublisher;

	// getter
	public Long count() {
		return assignmentRepository.count();
	}

	public List<Assignment> findAll() {
		return assignmentRepository.findAll();
	}
	public TreeMap<Date, List<Assignment>> getUpcomingAssignmentForSelectedCourse(){
		List<Assignment> assignmentList = this.findAll();
		return assignmentList.stream()
				.filter(assignment -> assignment.getCourse().getSelected())
				.filter(assignment -> Objects.nonNull(assignment.getDueAt()))
				.filter(assignment -> !assignment.getDueAt().before(new Date()))
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

	public List<Assignment> getAssignmentsByCourseId(Long courseId){
		return assignmentRepository.findByCourseId(courseId);
	}

	// setter
	public void saveAll(List<Assignment> assignmentList) {
		assignmentRepository.saveAll(assignmentList);
	}


	@Override
	public void publishUpdateEvent() {
		databaseUpdatedEventPublisher.publishEvent(this, DatabaseUpdatedEventPublisher.UpdateEventType.ASSIGNMENT_UPDATED);
	}
}

package canvas.canvasapp.service;

import canvas.canvasapp.event.publisher.database.DatabaseUpdatedEventPublisher;
import canvas.canvasapp.model.Course;
import canvas.canvasapp.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService implements IUpdateEvent {
	@Autowired
	CourseRepository courseRepository;
	@Autowired
	DatabaseUpdatedEventPublisher databaseUpdatedEventPublisher;

	public Long count() {
		return courseRepository.count();
	}

	// getter
	public Optional<Course> findById(Long id) {
		return courseRepository.findById(id);
	}

	public Optional<Course> findByName(String name) {
		return courseRepository.findByName(name);
	}

	public List<Course> findAll() {
		return courseRepository.findAll();
	}

	public List<Course> findAllSelected() {
		return courseRepository.getBySelectedIsTrue();
	}

	// setter


	public void setAllSelectedFalse() {
		courseRepository.setAllSelectedToFalse();
	}

	public void saveAll(List<Course> courseList) {
		courseRepository.saveAll(courseList);
	}

	// other
	@Override
	public void publishUpdateEvent() {
		databaseUpdatedEventPublisher.publishEvent(this, DatabaseUpdatedEventPublisher.UpdateEventType.COURSE_UPDATED);
	}
}

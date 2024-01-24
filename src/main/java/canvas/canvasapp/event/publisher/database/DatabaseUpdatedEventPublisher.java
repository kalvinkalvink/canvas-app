package canvas.canvasapp.event.publisher.database;

import canvas.canvasapp.event.task.database.AssignmentUpdatedEvent;
import canvas.canvasapp.event.task.database.CourseUpdatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class DatabaseUpdatedEventPublisher {

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	public enum UpdateEventType {
		COURSE_UPDATED,
		ASSIGNMENT_UPDATED
	}


	public void publishEvent(Object source, UpdateEventType updateEventType) {
		switch (updateEventType) {
			case COURSE_UPDATED -> eventPublisher.publishEvent(new CourseUpdatedEvent(source));
			case ASSIGNMENT_UPDATED -> eventPublisher.publishEvent(new AssignmentUpdatedEvent(source));
		}
	}
}

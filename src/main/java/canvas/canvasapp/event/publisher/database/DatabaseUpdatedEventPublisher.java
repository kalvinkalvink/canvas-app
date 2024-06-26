package canvas.canvasapp.event.publisher.database;

import canvas.canvasapp.event.task.database.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class DatabaseUpdatedEventPublisher {

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	public enum UpdateEventType {
		COURSE_UPDATED,
		ASSIGNMENT_UPDATED,
		ANNOUNCEMENT_UPDATED,
		FILE_UPDATED,
		FOLDER_UPDATED
	}


	public void publishEvent(Object source, UpdateEventType updateEventType) {
		switch (updateEventType) {
			case COURSE_UPDATED -> eventPublisher.publishEvent(new CourseUpdatedEvent(source));
			case ASSIGNMENT_UPDATED -> eventPublisher.publishEvent(new AssignmentUpdatedEvent(source));
			case ANNOUNCEMENT_UPDATED -> eventPublisher.publishEvent(new AnnouncementUpdatedEvent(source));
			case FILE_UPDATED -> eventPublisher.publishEvent(new FileUpdatedEevnt(source));
			case FOLDER_UPDATED -> eventPublisher.publishEvent(new FolderUpdateEvent(source));
		}
	}
}

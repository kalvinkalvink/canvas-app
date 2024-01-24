package canvas.canvasapp.event.task.database;

import org.springframework.context.ApplicationEvent;

public class CourseUpdatedEvent extends ApplicationEvent {
	public CourseUpdatedEvent(Object source) {
		super(source);
	}
}

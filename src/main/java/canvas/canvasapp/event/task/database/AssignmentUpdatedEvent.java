package canvas.canvasapp.event.task.database;

import org.springframework.context.ApplicationEvent;

public class AssignmentUpdatedEvent extends ApplicationEvent {

	public AssignmentUpdatedEvent(Object source) {
		super(source);
	}
}

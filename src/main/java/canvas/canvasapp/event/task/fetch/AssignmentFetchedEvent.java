package canvas.canvasapp.event.task.fetch;

import org.springframework.context.ApplicationEvent;

public class AssignmentFetchedEvent extends ApplicationEvent {

	public AssignmentFetchedEvent(Object source) {
		super(source);
	}
}

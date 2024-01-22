package canvas.canvasapp.event.task.fetch;

import org.springframework.context.ApplicationEvent;

public class CourseFetchedEvent extends ApplicationEvent {
	public CourseFetchedEvent(Object source) {
		super(source);
	}
}

package canvas.canvasapp.event.task.database;

import org.springframework.context.ApplicationEvent;

public class AnnouncementUpdatedEvent extends ApplicationEvent {
	public AnnouncementUpdatedEvent(Object source) {
		super(source);
	}
}

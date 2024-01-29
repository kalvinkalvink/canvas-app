package canvas.canvasapp.event.task.database;

import org.springframework.context.ApplicationEvent;

public class FolderUpdateEvent extends ApplicationEvent {
	public FolderUpdateEvent(Object source) {
		super(source);
	}
}

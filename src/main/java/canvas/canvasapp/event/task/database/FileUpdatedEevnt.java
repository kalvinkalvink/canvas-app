package canvas.canvasapp.event.task.database;

import org.springframework.context.ApplicationEvent;

public class FileUpdatedEevnt extends ApplicationEvent {
	public FileUpdatedEevnt(Object source) {
		super(source);
	}
}

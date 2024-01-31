package canvas.canvasapp.event.setting;

import org.springframework.context.ApplicationEvent;

public class CanvasApiIInfoUpdatedEvent extends ApplicationEvent {
	public CanvasApiIInfoUpdatedEvent(Object source) {
		super(source);
	}
}

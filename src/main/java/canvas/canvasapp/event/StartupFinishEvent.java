package canvas.canvasapp.event;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import org.springframework.context.ApplicationEvent;

public class StartupFinishEvent extends ApplicationEvent {
	public StartupFinishEvent(Object source) {
		super(source);
	}
}

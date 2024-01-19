package canvas.canvasapp.event;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;

public class StartupFinishEvent implements EventHandler {
	@Override
	public void handle(Event event) {
		System.out.println("finish startup");
	}
}

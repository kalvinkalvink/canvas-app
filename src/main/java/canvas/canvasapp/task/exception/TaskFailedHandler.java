package canvas.canvasapp.task.exception;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TaskFailedHandler implements EventHandler<WorkerStateEvent> {

	@Override
	public void handle(WorkerStateEvent workerStateEvent) {
		Worker worker = workerStateEvent.getSource();

		if (worker.getException() != null) {
			log.error(String.format("Task %s error", worker.getClass().getName()), worker.getException());
		}
	}
}

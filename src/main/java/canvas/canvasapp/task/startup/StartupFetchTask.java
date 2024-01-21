package canvas.canvasapp.task.startup;

import canvas.canvasapp.task.exception.TaskFailedHandler;
import canvas.canvasapp.task.fetch.FetchCourseTask;
import canvas.canvasapp.task.fetch.FetchSelectedAssignmentTask;
import javafx.concurrent.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.concurrent.ScheduledExecutorService;

@Service
@Scope("prototype")
public class StartupFetchTask extends Task<Void> {
	@Autowired
	FetchCourseTask fetchCourseTask;
	@Autowired
	FetchSelectedAssignmentTask fetchSelectedAssignmentTask;
	@Override
	protected Void call() {
		fetchCourseTask.setOnFailed(new TaskFailedHandler());
		fetchSelectedAssignmentTask.setOnFailed(new TaskFailedHandler());
		fetchCourseTask.run();
		fetchSelectedAssignmentTask.run();

		return null;
	}
}

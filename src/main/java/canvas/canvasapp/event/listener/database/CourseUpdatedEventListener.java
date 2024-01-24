package canvas.canvasapp.event.listener.database;

import canvas.canvasapp.event.listener.ApplicationListenerBase;
import canvas.canvasapp.event.task.database.CourseUpdatedEvent;
import canvas.canvasapp.task.fetch.FetchSelectedAssignmentTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class CourseUpdatedEventListener extends ApplicationListenerBase implements ApplicationListener<CourseUpdatedEvent> {
	@Autowired
	FetchSelectedAssignmentTask fetchSelectedAssignmentTask;
	@Override
	public void onApplicationEvent(CourseUpdatedEvent event) {
		// update assignments from canvas
		fixedThreadPoolExecutor.executeTask(fetchSelectedAssignmentTask);
	}
}

package canvas.canvasapp.event.listener.task.fetch;

import canvas.canvasapp.event.listener.ApplicationListenerBase;
import canvas.canvasapp.event.task.fetch.CourseFetchedEvent;
import canvas.canvasapp.task.fetch.FetchSelectedAssignmentTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class CourseFetchedEventListener extends ApplicationListenerBase implements ApplicationListener<CourseFetchedEvent> {
	@Autowired
	FetchSelectedAssignmentTask fetchSelectedAssignmentTask;
	@Override
	public void onApplicationEvent(CourseFetchedEvent event) {
		// update assignments from canvas
		fixedThreadPoolExecutor.executeTask(fetchSelectedAssignmentTask);
	}
}

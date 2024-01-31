package canvas.canvasapp.event.listener.database;

import canvas.canvasapp.event.listener.ApplicationListenerBase;
import canvas.canvasapp.event.task.database.FolderUpdateEvent;
import canvas.canvasapp.task.fetch.FetchSelectedCourseFileTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class FolderUpdatedEventListener extends ApplicationListenerBase implements ApplicationListener<FolderUpdateEvent> {
	@Autowired
	private FetchSelectedCourseFileTask fetchSelectedCourseFileTask;

	@Override
	public void onApplicationEvent(FolderUpdateEvent event) {
		fixedThreadPoolExecutor.executeTask(fetchSelectedCourseFileTask);
	}
}

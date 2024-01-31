package canvas.canvasapp.event.listener.database;

import canvas.canvasapp.event.listener.ApplicationListenerBase;
import canvas.canvasapp.event.task.database.CourseUpdatedEvent;
import canvas.canvasapp.task.fetch.FetchSelectedCourseAnnouncementTask;
import canvas.canvasapp.task.fetch.FetchSelectedCourseAssignmentTask;
import canvas.canvasapp.task.fetch.FetchSelectedCourseFolderTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class CourseUpdatedEventListener extends ApplicationListenerBase implements ApplicationListener<CourseUpdatedEvent> {
	@Autowired
	private FetchSelectedCourseAssignmentTask fetchSelectedCourseAssignmentTask;
	@Autowired
	private FetchSelectedCourseAnnouncementTask fetchSelectedCourseAnnouncementTask;
	@Autowired
	private FetchSelectedCourseFolderTask fetchSelectedCourseFolderTask;

	@Override
	public void onApplicationEvent(CourseUpdatedEvent event) {
		// update assignments from canvas
		fixedThreadPoolExecutor.executeTask(fetchSelectedCourseAssignmentTask);
		fixedThreadPoolExecutor.executeTask(fetchSelectedCourseAnnouncementTask);
		fixedThreadPoolExecutor.executeTask(fetchSelectedCourseFolderTask);

	}
}

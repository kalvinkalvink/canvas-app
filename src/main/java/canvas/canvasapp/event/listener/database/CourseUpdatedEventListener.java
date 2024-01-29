package canvas.canvasapp.event.listener.database;

import canvas.canvasapp.event.listener.ApplicationListenerBase;
import canvas.canvasapp.event.task.database.CourseUpdatedEvent;
import canvas.canvasapp.task.fetch.FetchSelectedAssignmentTask;
import canvas.canvasapp.task.fetch.FetchSelectedCourseAnnouncementTask;
import canvas.canvasapp.task.fetch.FetchSelectedCourseFileTask;
import canvas.canvasapp.task.fetch.FetchSelectedCourseFolderTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class CourseUpdatedEventListener extends ApplicationListenerBase implements ApplicationListener<CourseUpdatedEvent> {
	@Autowired
	FetchSelectedAssignmentTask fetchSelectedAssignmentTask;
	@Autowired
	FetchSelectedCourseAnnouncementTask fetchSelectedCourseAnnouncementTask;
	@Autowired
	FetchSelectedCourseFolderTask fetchSelectedCourseFolderTask;
	@Autowired
	FetchSelectedCourseFileTask fetchSelectedCourseFileTask;
	@Override
	public void onApplicationEvent(CourseUpdatedEvent event) {
		// update assignments from canvas
		fixedThreadPoolExecutor.executeTask(fetchSelectedAssignmentTask);
		fixedThreadPoolExecutor.executeTask(fetchSelectedCourseAnnouncementTask);
		fixedThreadPoolExecutor.executeTask(fetchSelectedCourseFolderTask);
		fixedThreadPoolExecutor.executeTask(fetchSelectedCourseFileTask);
	}
}
